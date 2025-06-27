USE FREERIDER_DB;

-- DELETE FROM RESERVATION;
DELETE FROM VEHICLE;
DELETE FROM CUSTOMER;

INSERT INTO CUSTOMER (ID, NAME, CONTACT, STATUS) VALUES
    (1000, 'Meyer, Eric', 'eme22@gmail.com', 'Active'),
    (1001, 'Sommer, Tina', '030 22458 29425', 'Active'),
    (1002, 'Schulze, Tim', '+49 171 2358124', 'InRegistration')
;

INSERT INTO VEHICLE (ID, MAKE, MODEL, SEATS, CATEGORY, POWER, STATUS) VALUES
    (8000, 'VW', 'ID.3', 4, 'Sedan', 'Electric', 'Active'),
    (8001, 'VW', 'Golf', 4, 'Sedan', 'Gasoline', 'Active'),
    (8002, 'VW', 'Golf', 4, 'Sedan', 'Hybrid', 'Active'),
    (8003, 'BMW', '320d', 4, 'Sedan', 'Diesel', 'Active'),
    (8004, 'Mercedes', 'EQS', 4, 'Sedan', 'Electric', 'Active'),
    (8005, 'VW', 'Multivan Life', 8, 'Van', 'Gasoline', 'Active'),
    (8006, 'Tesla', 'Model 3', 4, 'Sedan', 'Electric', 'Active'),
    (8007, 'Tesla', 'Model S', 4, 'Sedan', 'Electric', 'Serviced')
;

-- Mind DATETIME values in table RESERVATION converted from String formats:
-- - STR_TO_DATE('04/07/2025 20:00','%d/%m/%Y %H:%i') or
-- - STR_TO_DATE('04/07/2025 20:00:00','%d/%m/%Y %H:%i:%s'), see
-- https://www.w3schools.com/mysql/func_mysql_str_to_date.asp
--
-- Alternatively, a non-linear decimal format uses 4 digits for the year,
-- followed by 2 digits for month, day, hour, min and sec:
--    2025 * 10,000,000,000 - years
--    + 07 *    100,000,000 - month
--    + 18 *      1,000,000 - day
--    + 18 *         10,000 - hour
--    + 00 *            100 - minute
--    + 00 *              1 = 20250718180000 = '2025.07.18' time: '18:00:00'
-- The non-linear coding cannot be used for time arithmetic.
--
-- Unix time is a linear date/time counting seconds (32 bit) or milliseconds
-- (64 bit) since 01/01/1970 referencing UTC (Universal Time Coordinated).
-- Use https://www.epochconverter.com to convert date and time.
-- For example, 04.12.2024 10:00 corresponds to 1,670,148,000 (~1,6 Mrd sec).
-- 1734516000(sec), 1734516000000(msec) == Sunday, December 18, 2024 10:00:00
-- 1734537600000(msec) - Sunday, December 18, 2024 16:00:00.000
--
-- DATETIME adjustments
-- https://stackoverflow.com/questions/70442279/invalid-datetimeformat-when-inserting-date-in-h2-in-memory-db
-- INSERT INTO RESERVATION (ID, CUSTOMER_ID, VEHICLE_ID, RBEGIN, REND, PICKUP, DROPOFF, STATUS) VALUES
--     (682351, 1002, 8003, {ts '2020-08-07 00:00:00'}, {ts '2021-12-31 23:59:59'}, 'Potsdam', 'Teltow', 'Inquired'),
--     (682352, 1002, 8003, parsedatetime('2020-04-03-14.00.00', 'yyyy-MM-dd-HH.mm.ss'), parsedatetime('2020-04-03-14.00.00', 'yyyy-MM-dd-HH.mm.ss'), 'Potsdam', 'Teltow', 'Inquired')
-- ;

-- INSERT INTO RESERVATION (ID, CUSTOMER_ID, VEHICLE_ID, RBEGIN, REND, PICKUP, DROPOFF, STATUS) VALUES
--     (201235, 1000, 8002, STR_TO_DATE('20/07/2025 10:00:00','%d/%m/%Y %H:%i:%s'), STR_TO_DATE('20/07/2025 20:00:00','%d/%m/%Y %H:%i:%s'), 'Berlin Wedding', 'Berlin Wedding', 'Booked'),
--     (145373, 1001, 8002, STR_TO_DATE('04/07/2025 20:00','%d/%m/%Y %H:%i'), STR_TO_DATE('04/07/2025 23:00','%d/%m/%Y %H:%i'), 'Berlin Wedding', 'Hamburg', 'Inquired'),
--     (382565, 1000, 8006, 20250718180000, 20250718181000, 'Berlin Wedding', 'Hamburg', 'Inquired'),
--     (351682, 1002, 8001, from_unixtime(unix_timestamp()), from_unixtime(unix_timestamp() + 2*60*60), 'Berlin Wedding', 'Hamburg', 'Inquired'),
--     (682351, 1002, 8003, from_unixtime(1752829200), from_unixtime(1752861600), 'Potsdam', 'Teltow', 'Inquired')
-- ;

INSERT INTO RESERVATION (ID, CUSTOMER_ID, VEHICLE_ID, RBEGIN, REND, PICKUP, DROPOFF, STATUS) VALUES
    (201235, 1000, 8002, {ts '2025-07-20 10:00'}, {ts '2025-07-20 20:00'}, 'Berlin Wedding', 'Berlin Wedding', 'Booked'),
    (145373, 1001, 8002, {ts '2025-07-04 20:00'}, {ts '2025-07-04 23:00'}, 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (382565, 1000, 8006, {ts '2025-07-18 18:00'}, {ts '2025-07-07 18:10'}, 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (351682, 1002, 8001, {ts '2025-06-11 08:00'}, {ts '2025-06-11 20:00'}, 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (682351, 1002, 8003, {ts '2025-07-18 09:00'}, {ts '2025-07-07 18:00'}, 'Potsdam', 'Teltow', 'Inquired')
;
