cmake_minimum_required(VERSION 3.4.1)

aux_source_directory(hook SRC_HOOK)
aux_source_directory(hook/arm SRC_HOOK_ARM)
aux_source_directory(hook/arm/instruction SRC_HOOK_ARM_INSTRUCTION)
aux_source_directory(hook/mshook SRC_HOOK_MSHOOK)

set(SOURCE_FILES ${SRC_HOOK} ${SRC_HOOK_ARM} ${SRC_HOOK_ARM_INSTRUCTION} ${SRC_HOOK_MSHOOK})
add_library(Hook STATIC ${SOURCE_FILES})

target_link_libraries(Hook android dl z log)
target_include_directories(Hook PUBLIC .)