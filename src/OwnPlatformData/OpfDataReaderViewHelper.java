package OwnPlatformData;

import org.opensplice.dds.dcps.Utilities;

public final class OpfDataReaderViewHelper
{

    public static OpfDataReaderView narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfDataReaderView) {
            return (OpfDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static OpfDataReaderView unchecked_narrow(Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof OpfDataReaderView) {
            return (OpfDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
