#include <idc.idc>
static main() {
    auto i;
    auto eax, ebx;
    AddBpt(0x00F83E65);
    StartDebugger("","","");
    for(i = 0; i < 999; i++) {
        auto code = GetDebuggerEvent(WFNE_SUSP|WFNE_CONT, -1);
        eax = GetRegValue("eax");
        ebx = GetRegValue("ebx");
        Message("heapString[%d] = \"", eax);
        while(1) {
            if (Byte(ebx) == 0x00)
                break;
            Message("%s", Byte(ebx));
            ebx = ebx + 1;
        }
        Message("\";\n");
    }
}