package OwnPlatformData;

public final class Opf {

    public int opfID;
    public double absolute_speed;
    public double absolute_course;
    public int speed_source_id;
    public int course_source_id;
    public int heading_source_id;

    public Opf() {
    }

    public Opf(
        int _opfID,
        double _absolute_speed,
        double _absolute_course,
        int _speed_source_id,
        int _course_source_id,
        int _heading_source_id)
    {
        opfID = _opfID;
        absolute_speed = _absolute_speed;
        absolute_course = _absolute_course;
        speed_source_id = _speed_source_id;
        course_source_id = _course_source_id;
        heading_source_id = _heading_source_id;
    }

}
