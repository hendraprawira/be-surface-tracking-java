package OwnPlatformData;

public interface OpfTypeSupportOperations extends
    DDS.TypeSupportOperations
{
    @Override
    int register_type(
            DDS.DomainParticipant participant, 
            String type_name);

}
