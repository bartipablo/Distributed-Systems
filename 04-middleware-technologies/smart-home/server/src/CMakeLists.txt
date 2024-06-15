get_filename_component(smart_home_proto "../../protos/smart-home.proto" ABSOLUTE)
get_filename_component(smart_home_proto_path "${smart_home_proto}" PATH)

set(smart_home_proto_sources "${CMAKE_CURRENT_BINARY_DIR}/smart-home.pb.cc")
set(smart_home_proto_headers "${CMAKE_CURRENT_BINARY_DIR}/smart-home.pb.h")
set(smart_home_grpc_sources "${CMAKE_CURRENT_BINARY_DIR}/smart-home.grpc.pb.cc")
set(smart_home_grpc_headers "${CMAKE_CURRENT_BINARY_DIR}/smart-home.grpc.pb.h")
add_custom_command(
        OUTPUT "${smart_home_proto_sources}" "${smart_home_proto_headers}" "${smart_home_grpc_sources}" "${smart_home_grpc_headers}"
        COMMAND ${_PROTOBUF_PROTOC}
        ARGS --grpc_out "${CMAKE_CURRENT_BINARY_DIR}" --cpp_out "${CMAKE_CURRENT_BINARY_DIR}"
        -I "${smart_home_proto_path}"
        --plugin=protoc-gen-grpc="${_GRPC_CPP_PLUGIN_EXECUTABLE}"
        "${smart_home_proto}"
        DEPENDS "${smart_home_proto}")

include_directories("${CMAKE_CURRENT_BINARY_DIR}")

add_library(smart_home_grpc_proto
        ${smart_home_proto_sources}
        ${smart_home_proto_headers}
        ${smart_home_grpc_sources}
        ${smart_home_grpc_headers})
target_link_libraries(smart_home_grpc_proto
        ${_PROTOBUF_LIBPROTOBUF}
        ${_REFLECTION}
        ${_GRPC_GRPCPP})

foreach(_target smart_home_client smart_home_server smart_home_async_client smart_home_async_server)
    add_executable(${_target} "${_target}.cc")
    target_link_libraries(${_target}
            smart_home_grpc_proto
            ${_PROTOBUF_LIBPROTOBUF}
            ${_REFLECTION}
            ${_GRPC_GRPCPP})
endforeach()