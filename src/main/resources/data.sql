insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km) value ('Price list 1', 0, 15, 25, 3);
insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km) value ('Summer Price List', 10, 20, 30, 4);

insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location ) value ('2020-05-05','2020-06-05','0',false,1,1,'Novi Sad');
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location ) value ('2020-06-01','2020-06-30','0',true,2,1,'Novi Sad');
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location ) value ('2020-04-01','2020-04-30','0',false,3,2, 'Beograd');