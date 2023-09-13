package OwnPlatformData;

public interface OpfDataWriterOperations extends
    DDS.DataWriterOperations
{

    long register_instance(
            Opf instance_data);

    long register_instance_w_timestamp(
            Opf instance_data,
            DDS.Time_t source_timestamp);

    int unregister_instance(
            Opf instance_data,
            long handle);

    int unregister_instance_w_timestamp(
            Opf instance_data,
            long handle, 
            DDS.Time_t source_timestamp);

    int write(
            Opf instance_data,
            long handle);

    int write_w_timestamp(
            Opf instance_data,
            long handle, 
            DDS.Time_t source_timestamp);

    int dispose(
            Opf instance_data,
            long instance_handle);

    int dispose_w_timestamp(
            Opf instance_data,
            long instance_handle, 
            DDS.Time_t source_timestamp);
    
    int writedispose(
            Opf instance_data,
            long instance_handle);

    int writedispose_w_timestamp(
            Opf instance_data,
            long instance_handle, 
            DDS.Time_t source_timestamp);

    int get_key_value(
            OpfHolder key_holder,
            long handle);
    
    long lookup_instance(
            Opf instance_data);

}
