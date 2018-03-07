//
// Created by wangz on 2018/3/8 0008.
//

#include <jni.h>
#include "Common.h"
#include "DexFile.h"

#ifndef PROTECTAPK_MANDROID_H
#define PROTECTAPK_MANDROID_H

#endif //PROTECTAPK_MANDROID_H

/*
 * Use this to keep track of mapped segments.
 */
typedef struct MemMapping {
    void*   addr;           /* start of data */
    size_t  length;         /* length of data */

    void*   baseAddr;       /* page-aligned base address */
    size_t  baseLength;     /* length of mapping */
} MemMapping;


/*
 * Some additional VM data structures that are associated with the DEX file.
 */
struct DvmDex {
    /* pointer to the DexFile we're associated with */
    DexFile*            pDexFile;

    /* clone of pDexFile->pHeader (it's used frequently enough) */
    const DexHeader*    pHeader;

    /* interned strings; parallel to "stringIds" */
    struct StringObject** pResStrings;

    /* resolved classes; parallel to "typeIds" */
    struct ClassObject** pResClasses;

    /* resolved methods; parallel to "methodIds" */
    struct Method**     pResMethods;

    /* resolved instance fields; parallel to "fieldIds" */
    /* (this holds both InstField and StaticField) */
    struct Field**      pResFields;

    /* interface method lookup cache */
    struct AtomicCache* pInterfaceCache;

    /* shared memory region with file contents */
    bool                isMappedReadOnly;
    MemMapping          memMap;

    jobject dex_object;

    /* lock ensuring mutual exclusion during updates */
    pthread_mutex_t     modLock;
};


/*
 * Structure representing a "raw" DEX file, in its unswapped unoptimized
 * state.
 */
struct RawDexFile {
    char*       cacheFileName;
    DvmDex*     pDvmDex;
};


/*
 * One entry in the hash table.
 */
typedef struct ZipHashEntry {
    const char*     name;
    unsigned short  nameLen;
    //unsigned int    hash;
} ZipHashEntry;


/*
 * Read-only Zip archive.
 *
 * We want "open" and "find entry by name" to be fast operations, and we
 * want to use as little memory as possible.  We memory-map the file,
 * and load a hash table with pointers to the filenames (which aren't
 * null-terminated).  The other fields are at a fixed offset from the
 * filename, so we don't need to extract those (but we do need to byte-read
 * and endian-swap them every time we want them).
 *
 * To speed comparisons when doing a lookup by name, we could make the mapping
 * "private" (copy-on-write) and null-terminate the filenames after verifying
 * the record structure.  However, this requires a private mapping of
 * every page that the Central Directory touches.  Easier to tuck a copy
 * of the string length into the hash table entry.
 */
typedef struct ZipArchive {
    /* open Zip archive */
    int         mFd;

    /* mapped file */
    MemMapping  mMap;

    /* number of entries in the Zip archive */
    int         mNumEntries;

    /*
     * We know how many entries are in the Zip archive, so we can have a
     * fixed-size hash table.  We probe on collisions.
     */
    int         mHashTableSize;
    ZipHashEntry* mHashTable;
} ZipArchive;


/*
 * This represents an open, scanned Jar file.  (It's actually for any Zip
 * archive that happens to hold a Dex file.)
 */
struct JarFile {
    ZipArchive  archive;
    //MemMapping  map;
    char*       cacheFileName;
    DvmDex*     pDvmDex;
};

/*
 * Internal struct for managing DexFile.
 */
struct DexOrJar {
    char*       fileName;
    bool        isDex;
    bool        okayToFree;
    RawDexFile* pRawDexFile;
    JarFile*    pJarFile;
    u1*         pDexMemory; // malloc()ed memory, if any
};













































































