package OwnPlatformData;

public class OpfTypeSupport extends org.opensplice.dds.dcps.TypeSupportImpl implements DDS.TypeSupportOperations
{
    private static final long serialVersionUID = 1L;

    private long copyCache;

    public OpfTypeSupport()
    {
        super("OwnPlatformData::Opf",
              "",
              "opfID",
              null,
              OpfMetaHolder.metaDescriptor);
    }

    @Override
    protected DDS.DataWriter create_datawriter ()
    {
        return new OpfDataWriterImpl(this);
    }

    @Override
    protected DDS.DataReader create_datareader ()
    {
        return new OpfDataReaderImpl(this);
    }

    @Override
    protected DDS.DataReaderView create_dataview ()
    {
        return new OpfDataReaderViewImpl(this);
    }
}
