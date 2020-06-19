insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km, price_for_cdw, owner_id, removed) value ('Price list - agent 1', 0, 15, 25, 3, 100, 2, false);
insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km, price_for_cdw, owner_id, removed) value ('Price list - client 1', 10, 15, 30, 4, 150, 3, false);
insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km, price_for_cdw, owner_id, removed) value ('Price list - client 2', 15, 20, 30, 130, 3, 4, false);
insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km, price_for_cdw, owner_id, removed) value ('Price list - client 3', 0, 0, 30, 5, 240, 5, false);
insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km, price_for_cdw, owner_id, removed) value ('Price list - agent 2', 15, 20, 30, 4, 70,  6, false);

insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-01','2020-06-25','0',false,1,1,'Novi Sad',2, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-01','2020-06-30','0',true,2,1,'Novi Sad',2, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-04-01','2020-04-30','0',false,3,2, 'Beograd',3, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-03','2020-06-19','500',false,4,2, 'Beograd',3, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-01','2020-06-30','700',false,5,5, 'Novi Sad',6, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-01','2020-06-18','0',false,6,5, 'Novi Sad',6, true);

insert into user_can_post_comment ( user_id, car_id, posted, request_end_date) value ( 3, 1, false, '2020-06-05');
insert into user_can_post_comment ( user_id, car_id, posted, request_end_date) value ( 3, 5, false, '2020-06-30');
insert into user_can_post_comment ( user_id, car_id, posted, request_end_date) value ( 5, 1, false, '2020-06-20');
insert into user_can_post_comment ( user_id, car_id, posted, request_end_date) value ( 5, 2, false, '2020-06-06');

insert into user_can_rate (user_id, car_id, rated, request_end_date) value (3, 1, false, '2020-06-05');


