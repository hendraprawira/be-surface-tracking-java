package OwnPlatformData;

import org.opensplice.dds.dcps.Utilities;

public final class OpfTypeSupportHelper
{

    public static OpfTypeSupport narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfTypeSupport) {
            return (OpfTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static OpfTypeSupport unchecked_narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfTypeSupport) {
            return (OpfTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
