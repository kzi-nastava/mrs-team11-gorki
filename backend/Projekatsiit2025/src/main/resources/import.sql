-- ČIST START
-- =======================
-- KORISNICI I VOZAČI / PUTNICI
-- =======================
-- Prvi vozač
INSERT INTO "user" (
    id, email, password, first_name, last_name,
    phone_number, address, profile_image,
    active, blocked, role
) VALUES (
    1,
    'driver1@gmail.com',
    'pass',
    'Marko',
    'Markovic',
    611111111,
    'Novi Sad',
    NULL,
    true,
    false,
    1
);

-- Prva putnica
INSERT INTO "user" (
    id, email, password, first_name, last_name,
    phone_number, address, profile_image,
    active, blocked, role
) VALUES (
    2,
    'passenger1@gmail.com',
    'pass',
    'Ana',
    'Anic',
    622222222,
    'Novi Sad',
    NULL,
    true,
    false,
    0
);

-- Drugi vozač
INSERT INTO "user" (
    id, email, password, first_name, last_name,
    phone_number, address, profile_image,
    active, blocked, role
) VALUES (
    3,
    'driver2@gmail.com',
    'pass',
    'Jovan',
    'Jovic',
    633333333,
    'Novi Sad',
    NULL,
    true,
    false,
    1
);

-- Druga putnica
INSERT INTO "user" (
    id, email, password, first_name, last_name,
    phone_number, address, profile_image,
    active, blocked, role
) VALUES (
    4,
    'passenger2@gmail.com',
    'pass',
    'Maja',
    'Majic',
    644444444,
    'Novi Sad',
    NULL,
    true,
    false,
    0
);

-- =======================
-- DRIVER & PASSENGER
-- =======================

INSERT INTO driver (id, vehicle_id, status, activity_last24h) VALUES
(1, NULL, 'ACTIVE', 5.5),
(3, NULL, 'ACTIVE', 4.2);

INSERT INTO passenger (id) VALUES
(2),
(4);

-- =======================
-- RUTE
-- =======================

INSERT INTO route (id, distance) VALUES
(1, 5.2),
(2, 8.0),
(3, 12.5);

-- =======================
-- VOŽNJE
-- =======================

-- Vožnja 1: driver1 i passenger1
INSERT INTO ride (
    id, status, price, scheduled_time, starting_time, ending_time,
    panic_activated, cancellation_reason, cancelled_by,
    driver_id, route_id, creator_id
) VALUES (
    1, 'FINISHED', 750.0, '2025-01-20 09:50:00', '2025-01-20 10:00:00', '2025-01-20 10:25:00',
    false, NULL, NULL,
    1, 1, 2
);

-- Vožnja 2: driver2 i passenger2
INSERT INTO ride (
    id, status, price, scheduled_time, starting_time, ending_time,
    panic_activated, cancellation_reason, cancelled_by,
    driver_id, route_id, creator_id
) VALUES (
    2, 'FINISHED', 1200.0, '2025-01-21 14:00:00', '2025-01-21 14:10:00', '2025-01-21 14:50:00',
    false, NULL, NULL,
    3, 2, 4
);

-- Vožnja 3: driver1 i oba putnika
INSERT INTO ride (
    id, status, price, scheduled_time, starting_time, ending_time,
    panic_activated, cancellation_reason, cancelled_by,
    driver_id, route_id, creator_id
) VALUES (
    3, 'FINISHED', 900.0, '2025-01-22 09:00:00', '2025-01-22 09:15:00', '2025-01-22 09:45:00',
    false, NULL, NULL,
    1, 3, 2
);

-- Vožnja 4: driver2 i oba putnika
INSERT INTO ride (
    id, status, price, scheduled_time, starting_time, ending_time,
    panic_activated, cancellation_reason, cancelled_by,
    driver_id, route_id, creator_id
) VALUES (
    4, 'FINISHED', 1500.0, '2025-01-23 11:00:00', '2025-01-23 11:05:00', '2025-01-23 11:50:00',
    false, NULL, NULL,
    3, 3, 4
);

-- =======================
-- VOŽNJE - PASSENGERI
-- =======================

INSERT INTO ride_passengers (ride_id, passenger_id) VALUES
(1, 2),
(2, 4),
(3, 2),
(3, 4),
(4, 2),
(4, 4);

-- =======================
-- VOZILA - VOZACI
-- =======================

INSERT INTO vehicle (
    baby_transport,
    latitude,
    longitude,
    pet_friendly,
    seats,
    id,
    address,
    model,
    plate_number,
    type
) VALUES
(
    false,
    45.2671,
    19.8335,
    false,
    4,
    1,
    'Bulevar Oslobođenja 10, Novi Sad',
    'Toyota Corolla',
    'NS-101-AA',
    'STANDARD'
),
(
    true,
    45.2517,
    19.8369,
    true,
    6,
    2,
    'Cara Dušana 5, Novi Sad',
    'Volkswagen Sharan',
    'NS-202-BB',
    'LUXURY'
);

UPDATE driver
SET vehicle_id = 1
WHERE id = 1;

UPDATE driver
SET vehicle_id = 2
WHERE id = 3;

--Za ride in progress
INSERT INTO ride(panic_activated,price,price_by_vehicle_type,price_per_km,
				creator_id,driver_id,ending_time,id,route_id,scheduled_time,starting_time,
				cancellation_reason,cancelled_by,status)
VALUES(
 false,
 1000,
 null,
 null,
 4,
 1,
 '2026-01-27 19:50:00',
 5,
 3,
 '2026-01-27 16:00:00',
 '2026-01-27 19:00:00',
 null,
 null,
 'STARTED'
)

INSERT INTO ride (
	id,
	price_by_vehicle_type,
	price_per_km,
    status,
    price,
    panic_activated,
    cancelled_by,
    cancellation_reason,
    driver_id,
    route_id,
    creator_id,
    scheduled_time,
    starting_time,
    ending_time,
    paid
) VALUES (
	6,
	null,
	null,
    'ACCEPTED',       -- zakazana
    800.0,             -- cena
    false,             -- panic
    NULL,              -- cancelled_by
    NULL,              -- cancellation_reason
    1,                 -- driver_id = 1
    1,                 -- route_id, može ista ruta kao prethodna
    2,                 -- creator_id, neka putnica
    '2026-01-28 09:00:00', -- scheduled_time
    NULL,              -- starting_time
    NULL,              -- ending_time
    false              -- paid
);

UPDATE ride
	SET starting_time='2026-01-27 20:30:00'
		where id=6;
		
UPDATE ride
	SET ending_time='2026-01-27 21:30:00'
		where id=6