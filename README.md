Для запуска в linux выполнить run.sh лежащий в папке out/artifacts.
Для подключения драйвера скопировать .jar файл в папку connector папки из которой запускается run.sh. 
И запустить с параметром -d <драйвер>

- -d Класс драйвера для подключения к бд значение по умолчанию com.mysql.jdbc.Driver
- -db путь для подключения к базе данных значение по умолчанию jdbc:mysql://localhost/fastentest 
- -dt таблица из которой будут читаться данные значение по умолчанию test_data
- -u пользователь для подключения к БД значение по умолчанию root
- -p пароль для подключения к БД по умолчанию пусто
- -rf папка в которую будет загружен результат значение по умолчанию result. Папка должна быть создана заранее. (сейчас папка для выгрузки из бд равна папке для загрузки обратно)
- -rfl файл в который будет загружен результат значение по умолчанию result.csv.gz  (сейчас файл для выгрузки из бд равна папке для загрузки обратно)
- -to таймаут. Количество секунд которое Consumer ожидает данных. После чего отключается. По умолчанию 5
- -req тип запроса к бд значение (возможные значения: 
- select_all все результаты из бд (по умолчанию)
- select_range результаты за конкретный промежуток <-f нижняя граница> <-t верхняя граница>
- select_by_date результаты за дату <-day день за который будет получен результат по умолчанию 2016-01-01>
- select_by_month результаты за месяц <-month месяц за который будет получен результат по умолчанию 01> <-year год за который будет получен результат по умолчанию 2016>
- select_by_year резульаты за год <-year год за который будет получен результат. по умолчанию 2016>)

пример -req select_range -f 2016-01-01 -t 2016-06-01 
