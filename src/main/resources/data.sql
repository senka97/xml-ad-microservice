insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km,owner_id) value ('Price list 1', 0, 15, 25, 3, 2);
insert into price_list (alias, discount20Days, discount30Days, price_per_day, price_per_km,owner_id) value ('Summer Price List', 10, 20, 30, 4, 3);


insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-05-05','2020-06-05','0',false,1,1,'Novi Sad',3, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-01','2020-06-30','0',true,2,1,'Novi Sad',3, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-04-01','2020-04-30','0',false,3,2, 'Beograd',3, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-03','2020-06-19','500',false,4,1, 'Novi Sad',2, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-01','2020-06-30','700',false,5,1, 'Beograd',2, true);
insert into ad (start_date, end_date, limit_km, cdw, car_id, price_list_id, location, owner_id, visible) value ('2020-06-01','2020-06-17','0',false,6,1, 'Novi Sad',2, true);

insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 4, true);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 4, true);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 4, true);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 4, true);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 4, false);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 4, false);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 5, true);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 5, true);
insert into user_can_post_comment ( user_id, ad_id, posted) value ( 3, 5, false);

