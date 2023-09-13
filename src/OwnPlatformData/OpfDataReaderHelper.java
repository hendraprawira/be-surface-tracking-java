package OwnPlatformData;

import org.opensplice.dds.dcps.Utilities;

public final class OpfDataReaderHelper
{

    public static OpfDataReader narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfDataReader) {
            return (OpfDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static OpfDataReader unchecked_narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfDataReader) {
            return (OpfDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
