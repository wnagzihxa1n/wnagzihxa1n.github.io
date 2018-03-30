//
// Created by wangz on 2018/3/12 0012.
//

#include "../native-lib.h"
#include "elf.h"

#ifndef GOTHOOK_GOTHOOK_H
#define GOTHOOK_GOTHOOK_H

class GotHook {
public:
    struct Config {
        bool check_ehdr;            // do verify so file elf header
        bool unprotect_got_memory;  // unprotect got table memory when parse data
        bool with_local_func;       // collect local func in the same time
    };
    GotHook(const char *mapName, Config* config = nullptr);
    bool is_valid() { return is_valid_; }
    bool rebindFunc(Elf32_Addr originalFunc, Elf32_Addr newFunc);
    Elf32_Addr loadFromMap(const char* name);
    bool Load();
    bool ReadElfHeader();
    bool VerifyElfHeader();
    bool FindPhdr();
    bool CheckPhdr(Elf32_Addr);
    bool ReadSoInfo();
    bool ReadGotInfo();
    static bool unProtectMemory(void* addr, uint32_t size);
    static bool protectMemory(void* addr, uint32_t size);
    void phdr_table_get_dynamic_section(const Elf32_Phdr* phdr_table,
                                        int               phdr_count,
                                        Elf32_Addr        load_bias,
                                        Elf32_Dyn**       dynamic,
                                        size_t*           dynamic_count,
                                        Elf32_Word*       dynamic_flags);
    std::string name;
    Elf32_Addr load_bias_ = 0;
    Elf32_Ehdr* header_ = nullptr;
    Elf32_Phdr *phdr_table_ = nullptr;
    size_t phdr_num_ = 0;
    const Elf32_Phdr *loaded_phdr_ = nullptr;
    bool is_valid_ = false;
    size_t plt_rel_count = 0;
    Elf32_Addr * plt_got = nullptr;
    Elf32_Addr * got_start = nullptr;
    Elf32_Addr * got_end = nullptr;
    Elf32_Dyn* dynamic_start = nullptr;
    size_t dynamic_count = 0;
private:
    bool check_ehdr = true;
    bool unprotect_got_memory = false;
    bool with_local_func = false;
};


// 初始化Hook环境，创建配置文件结构体Config
GotHook::GotHook(const char *libName, Config* config)
        : name(libName)
{
    if(config != nullptr) {
        check_ehdr = config->check_ehdr;
        unprotect_got_memory = config->unprotect_got_memory;
        with_local_func = config->with_local_func;
    }
    load_bias_ = loadFromMap(libName);// 获取传入so文件的加载基址
    is_valid_ = Load();
}

// 搜索该so文件在内存中的基址
Elf32_Addr GotHook::loadFromMap(const char *name) {
    auto fd = fopen("/proc/self/maps", "r");
    if(fd == nullptr) {
        return 0;
    }
    char buf[256];
    while(fgets(buf, 256, fd) != nullptr) {
        if(strstr(buf, name)) {
            fclose(fd);
            auto start = strtoul(buf, 0, 16);
            return start;
        }
    }
    fclose(fd);
    return 0;
}

// 读取该so文件数据并创建结构体
bool GotHook::Load() {
    return ReadElfHeader() &&
           (check_ehdr ? VerifyElfHeader(): true) &&
           FindPhdr() &&
           ReadSoInfo() &&
           ReadGotInfo();
}

bool GotHook::ReadElfHeader() {
    if(load_bias_ == 0) {
        LOGE("Unable to find so file %s in map", name.c_str());
        return false;
    }
    header_ = (Elf32_Ehdr*)load_bias_;
    return true;
}

bool GotHook::VerifyElfHeader() {
    if (header_->e_ident[EI_MAG0] != ELFMAG0 ||
        header_->e_ident[EI_MAG1] != ELFMAG1 ||
        header_->e_ident[EI_MAG2] != ELFMAG2 ||
        header_->e_ident[EI_MAG3] != ELFMAG3) {
        LOGE("link so %s has bad ELF magic", name.c_str());
        return false;
    }

    if (header_->e_ident[EI_CLASS] != ELFCLASS32) {
        LOGE("not 32-bit so file %s %d", name.c_str(), header_->e_ident[EI_CLASS]);
        return false;
    }

    if (header_->e_ident[EI_DATA] != ELFDATA2LSB) {
        LOGE("not little-endian %s %d", name.c_str(), header_->e_ident[EI_DATA]);
        return false;
    }

    if (header_->e_type != ET_DYN) {
        LOGE("has unexpected e_type %s %d", name.c_str(), header_->e_type);
        return false;
    }

    if (header_->e_version != EV_CURRENT) {
        LOGE("has unexpected e_version %s %d", name.c_str(), header_->e_version);
        return false;
    }

    return true;
}

bool GotHook::FindPhdr() {
    phdr_table_ = (Elf32_Phdr *) (header_->e_phoff + load_bias_);
    phdr_num_ = header_->e_phnum;

    const Elf32_Phdr* phdr_limit = phdr_table_ + phdr_num_;
    for (const Elf32_Phdr* phdr = phdr_table_; phdr < phdr_limit; ++phdr) {
        if (phdr->p_type == PT_PHDR) {
            return CheckPhdr(load_bias_ + phdr->p_vaddr);
        }
    }

    for (const Elf32_Phdr* phdr = phdr_table_; phdr < phdr_limit; ++phdr) {
        if (phdr->p_type == PT_LOAD) {
            if (phdr->p_offset == 0) {
                Elf32_Addr  elf_addr = load_bias_ + phdr->p_vaddr;
                const Elf32_Ehdr* ehdr = (const Elf32_Ehdr*)(void*)elf_addr;
                Elf32_Addr offset = ehdr->e_phoff;
                return CheckPhdr((Elf32_Addr)ehdr + offset);
            }
            break;
        }
    }
    LOGE("%s cant find loaded phdr", name.c_str());
    return false;
}

bool GotHook::ReadSoInfo() {
    Elf32_Word dynamic_flags;
    phdr_table_get_dynamic_section(loaded_phdr_, phdr_num_, load_bias_, &dynamic_start,
                                   &dynamic_count, &dynamic_flags);
    if(dynamic_start == nullptr) {
        LOGE("%s has No valid dynamic phdr data", name.c_str());
        return false;
    }

    for (Elf32_Dyn* d = dynamic_start; d->d_tag != DT_NULL; ++d) {
        switch(d->d_tag){
            case DT_PLTRELSZ:
                plt_rel_count = d->d_un.d_val / sizeof(Elf32_Rel);
                break;
            case DT_PLTGOT:
                plt_got = (Elf32_Addr *)(load_bias_ + d->d_un.d_ptr);
                break;
            default:
                break;
        }
    }
    return true;
}

bool GotHook::ReadGotInfo() {
    if(plt_got == nullptr) {
        return false;
    }

    got_start = plt_got;
    for(auto i = 0; i < 4; i++, got_start++) {
        if(*got_start != 0) {
            break;
        }
    }

    got_end = got_start + plt_rel_count;
    if(with_local_func) {
        got_start = (Elf32_Addr *) (dynamic_start + dynamic_count);
    }

    if(unprotect_got_memory) {
        unProtectMemory(got_start, got_end - got_start);
    }
    return true;
}

bool GotHook::CheckPhdr(Elf32_Addr loaded) {
    const Elf32_Phdr* phdr_limit = phdr_table_ + phdr_num_;
    Elf32_Addr loaded_end = loaded + (phdr_num_ * sizeof(Elf32_Phdr));
    for (Elf32_Phdr* phdr = phdr_table_; phdr < phdr_limit; ++phdr) {
        if (phdr->p_type != PT_LOAD) {
            continue;
        }
        Elf32_Addr seg_start = phdr->p_vaddr + load_bias_;
        Elf32_Addr seg_end = phdr->p_filesz + seg_start;
        if (seg_start <= loaded && loaded_end <= seg_end) {
            loaded_phdr_ = reinterpret_cast<const Elf32_Phdr*>(loaded);
            return true;
        }
    }
    LOGE("%s loaded phdr %x not in loadable segment", name.c_str(), loaded);
    return false;
}

bool GotHook::rebindFunc(Elf32_Addr originalFunc, Elf32_Addr newFunc) {
    if(!is_valid_) {
        LOGE("%s has no valid got information", name.c_str());
        return false;
    }

    for(auto func = got_start; func <= got_end; func++) {
        if(*func == originalFunc) {
            if(unprotect_got_memory) {
                *func = newFunc;
            } else {
                unProtectMemory(func, sizeof(Elf32_Addr));
                *func = newFunc;
                protectMemory(func, sizeof(Elf32_Addr));
            }

            return true;
        }
    }
//    LOGE("Unable to hook function %s %p into %p", name.c_str(), originalFunc, newFunc);
    return false;
}

bool GotHook::unProtectMemory(void *addr, uint32_t size) {
    auto page_size = sysconf(_SC_PAGESIZE);
    auto align = ((size_t)addr) % page_size;
    return mprotect((uint8_t*)addr - align, size + align, PROT_READ|PROT_WRITE) != -1;
}

bool GotHook::protectMemory(void *addr, uint32_t size) {
    auto page_size = sysconf(_SC_PAGESIZE);
    auto align = ((size_t)addr) % page_size;
    return mprotect((uint8_t*)addr - align, size + align, PROT_READ) != -1;
}

void GotHook::phdr_table_get_dynamic_section(const Elf32_Phdr* phdr_table,
                                               int               phdr_count,
                                               Elf32_Addr        load_bias,
                                               Elf32_Dyn**       dynamic,
                                               size_t*           dynamic_count,
                                               Elf32_Word*       dynamic_flags)
{
    const Elf32_Phdr* phdr = phdr_table;
    const Elf32_Phdr* phdr_limit = phdr + phdr_count;

    for (phdr = phdr_table; phdr < phdr_limit; phdr++) {
        if (phdr->p_type != PT_DYNAMIC) {
            continue;
        }

        *dynamic = reinterpret_cast<Elf32_Dyn*>(load_bias + phdr->p_vaddr);
        if (dynamic_count) {
            *dynamic_count = (unsigned)(phdr->p_memsz / sizeof(Elf32_Dyn));
        }
        if (dynamic_flags) {
            *dynamic_flags = phdr->p_flags;
        }
        return;
    }
    *dynamic = NULL;
    if (dynamic_count) {
        *dynamic_count = 0;
    }
}


#endif //GOTHOOK_GOTHOOK_H
