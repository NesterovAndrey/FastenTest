package core.sql;

//Запросы к БД.
//Лучше было бы хранить их во внешних файлах, но не стал тратить время на это.
public class SQLStrings {

    public static final String SELECT_ALL="SELECT * FROM %s LIMIT %s OFFSET %s";
    public static final String SELECT_ALL_BY_RANGE="SELECT * FROM %s WHERE reg_date BETWEEN '%s' AND '%s' LIMIT %s OFFSET %s";
    public static final String SELECT_ALL_BY_DAY="SELECT * FROM %s WHERE reg_date='%s' LIMIT %s OFFSET %s";
    public static final String SELECT_ALL_BY_MONTH="SELECT * FROM %s WHERE YEAR(reg_date)=%s AND MONTH(reg_date)=%s LIMIT %s OFFSET %s";
    public static final String SELECT_ALL_BY_YEAR="SELECT * FROM %s WHERE YEAR(reg_date)=%s LIMIT %s OFFSET %s ";

    public static final String CREATE_TABLE="CREATE TABLE IF NOT EXISTS `%s` (cdca VARCHAR(40),cn VARCHAR(17),reg_date DATE,owner_id  VARCHAR(255),owner_passport  VARCHAR(10));";
    public static final String INSERT_DATA="INSERT INTO `%s`  VALUES %s";
    public static final String VALUE_SQL="(%s),";
}
