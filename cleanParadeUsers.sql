DELETE from User where login not like '%.%' OR login like '%@%';
