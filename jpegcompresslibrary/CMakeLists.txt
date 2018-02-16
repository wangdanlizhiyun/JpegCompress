cmake_minimum_required(VERSION 3.4.1)



add_library( libjpeg STATIC IMPORTED)
set_target_properties(libjpeg PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/cpp/libs/libjpeg.a)



#设置头文件查找目录
include_directories(src/main/cpp/include)


file(GLOB my_source src/main/cpp/*.cpp src/main/cpp/*.c)
add_library(native-lib SHARED ${my_source})

#编译链接
target_link_libraries(native-lib libjpeg  jnigraphics log )