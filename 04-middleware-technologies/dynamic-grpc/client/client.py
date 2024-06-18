import grpc
import subprocess
from grpc_reflection.v1alpha.proto_reflection_descriptor_database import ProtoReflectionDescriptorDatabase
from google.protobuf.descriptor_pool import DescriptorPool

HOST='localhost'
PORT=50051


def type_name(type_code):
    type_map = {
        1: 'double',
        2: 'float',
        3: 'int64',
        4: 'uint64',
        5: 'int32',
        6: 'fixed64',
        7: 'fixed32',
        8: 'bool',
        9: 'string',
        10: 'group',
        11: 'message',
        12: 'bytes',
        13: 'uint32',
        14: 'enum',
        15: 'sfixed32',
        16: 'sfixed64',
        17: 'sint32',
        18: 'sint64'
    }
    return type_map.get(type_code, 'unknown')


def label_name(label_code):
    label_map = {
        1: 'LABEL_OPTIONAL',
        2: 'LABEL_REQUIRED',
        3: 'LABEL_REPEATED'
    }
    return label_map.get(label_code, 'unknown')


def get_service(desc_pool):
    service_desc = None
    service_name = None

    while service_desc is None:
        try:
            service_name = input("Type service name: ")
            service_desc = desc_pool.FindServiceByName(service_name)
            if service_desc is None:
                print("Service not found")
        except KeyError:
            print("Service not found")
        except Exception as e:
            print(f"Error: {e}")
            exit(1)
    return service_desc, service_name


def print_serive_methods(service_desc):
    print(f"Methods in service {service_desc.full_name}:")
    for method_name, method_desc in service_desc.methods_by_name.items():
        print(f"- {method_name}")

        input_type = method_desc.input_type
        print(f"  Input type: {input_type.full_name}")
        for field in input_type.fields:
            print(f"    Field name: {field.name}, type: {type_name(field.type)}, label: {label_name(field.label)}")


def main():
    
    channel = grpc.insecure_channel('localhost:50051')
    reflection_db = ProtoReflectionDescriptorDatabase(channel)
    desc_pool = DescriptorPool(reflection_db)

    print("Available services:")
    services = reflection_db.get_services()
    print(services)

    service_desc, service_name = get_service(desc_pool)

    print_serive_methods(service_desc)

    while (True):
        request = input(">>> ")

        if (request == 'exit'):
            break

        if (request == 'list'):
            print_serive_methods(service_desc)
            continue

        if (request == 'help'):
            print('example: Add {"addend_1":1,"addend_2":2}')
            continue

        try: 
            request = request.split()
            method_name = request[0]
            args = request[1]

            grpcurl_command = [
                'grpcurl',
                '-plaintext',
                '-d', args,
                f'{HOST}:{PORT}',
                f'{service_name}/{method_name}'
            ]   
            result = subprocess.run(grpcurl_command, capture_output=True, text=True)
            print(result.stdout)
            print(result.stderr)
        
        except IndexError:
            print("Invalid request")
            continue

        except Exception as e:
            print(f"Unexpected error: {e}")
            break

        
if __name__ == '__main__':
    main()