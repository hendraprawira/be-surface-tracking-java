package OwnPlatformData;

import org.opensplice.dds.dcps.Utilities;

public final class OpfDataWriterHelper
{

    public static OpfDataWriter narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfDataWriter) {
            return (OpfDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static OpfDataWriter unchecked_narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfDataWriter) {
            return (OpfDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
