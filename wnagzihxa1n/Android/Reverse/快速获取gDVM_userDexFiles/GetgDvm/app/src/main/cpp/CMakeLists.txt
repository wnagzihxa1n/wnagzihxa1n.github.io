cmake_minimum_required(VERSION 3.4.1)

add_subdirectory(Hook)
aux_source_directory(. RootSrc)

add_library(native-lib SHARED native-lib.cpp)
find_library(log-lib log)
target_link_libraries(native-lib Hook android dl z ${log-lib})