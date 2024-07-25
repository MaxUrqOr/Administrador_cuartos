-- phpMyAdmin SQL Dump
-- version 5.1.1deb5ubuntu1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 22-07-2024 a las 19:39:19
-- Versión del servidor: 8.0.37-0ubuntu0.22.04.3
-- Versión de PHP: 8.1.2-1ubuntu2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `Departamentos`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`admin`@`localhost` PROCEDURE `ActualizarDatosInquilinos` ()  BEGIN
    UPDATE Inquilinos AS I
    LEFT JOIN (
        SELECT 
            ID_INQUILINO,
            COUNT(*) AS total_solicitudes
        FROM
            Solicitudes
        GROUP BY ID_INQUILINO
    ) AS S ON I.ID_INQUILINO = S.ID_INQUILINO
    LEFT JOIN (
        SELECT 
            ID_INQUILINO,
            SUM(CASE WHEN Cuotas.ESTADO_CUO = 'Pendiente' THEN 1 ELSE 0 END) AS total_deudas
        FROM
            Cuartos
        INNER JOIN Cuotas ON Cuartos.ID_CUARTO = Cuotas.ID_CUARTO
        GROUP BY ID_INQUILINO
    ) AS D ON I.ID_INQUILINO = D.ID_INQUILINO
    LEFT JOIN (
        SELECT 
            ID_INQUILINO,
            COUNT(*) AS total_pagos
        FROM
            Pago
        INNER JOIN Cuotas ON Pago.ID_CUOTA = Cuotas.ID_CUOTA
        INNER JOIN Cuartos ON Cuotas.ID_CUARTO = Cuartos.ID_CUARTO
        GROUP BY Cuartos.ID_INQUILINO
    ) AS P ON I.ID_INQUILINO = P.ID_INQUILINO
    SET
        I.SOLICITUDES_INQ = IFNULL(S.total_solicitudes, 0),
        I.DEUDAS_INQ = IFNULL(D.total_deudas, 0),
        I.PAGOS_INQ = IFNULL(P.total_pagos, 0);
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ActualizarEstadoCuartos` ()  BEGIN
    UPDATE Cuartos AS C
    SET ESTADO_CUA = 
        CASE 
            WHEN (
                SELECT COUNT(*) 
                FROM Cuotas AS Q 
                WHERE Q.ID_CUARTO = C.ID_CUARTO 
                AND Q.ESTADO_CUO = 'Pendiente'
            ) > 3 THEN 'Morosidad Inminente'
            WHEN (
                SELECT COUNT(*) 
                FROM Cuotas AS Q 
                WHERE Q.ID_CUARTO = C.ID_CUARTO 
                AND Q.ESTADO_CUO = 'Pendiente'
            ) > 0 THEN 'En Deuda'
            ELSE 'Al Día'
        END
   WHERE C.ESTADO_CUA != 'Libre';
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ActualizarPagoFoto` (IN `p_ID_PAGO` INT, IN `p_FECHA_PAG` DATE, IN `p_RUTA_FOTO` VARCHAR(150), IN `p_ESTADO_PAG` VARCHAR(15), IN `p_RAZON_PAG` VARCHAR(150))  BEGIN
    UPDATE Pago
    SET FECHA_PAG = p_FECHA_PAG,
        RUTA_FOTO = p_RUTA_FOTO,
        ESTADO_PAG = p_ESTADO_PAG,
        RAZON_PAG = p_RAZON_PAG
    WHERE ID_PAGO = p_ID_PAGO;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `Actualizar_costo_garantia_cuarto` (IN `p_id_cuarto` INT, IN `p_nuevo_costo` INT, IN `p_nueva_garantia` INT)  BEGIN
    UPDATE Cuartos
    SET COSTO_CUA = p_nuevo_costo, GARANTIA_CUA = p_nueva_garantia
    WHERE ID_CUARTO = p_id_cuarto;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `actualizar_estados_cuotas_proc` ()  BEGIN
    DECLARE fecha_actual DATE;
    
    -- Obtener la fecha actual
    SET fecha_actual = CURRENT_DATE;
    
    -- Actualizar los estados de las cuotas
    UPDATE Cuotas
    SET ESTADO_CUO = CASE 
        WHEN FECHA_VENCIMIENTO > fecha_actual THEN 'Futuro'
        WHEN FECHA_VENCIMIENTO <= fecha_actual AND ESTADO_CUO != 'Pagado' AND ESTADO_CUO != 'Retirado' THEN 'Pendiente'
        ELSE ESTADO_CUO
    END;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `actualizar_estado_pago` (IN `pago_id` INT, IN `nuevo_estado` VARCHAR(15), IN `razon_pago` VARCHAR(255))  BEGIN
    -- Actualizar el estado del pago
    UPDATE Pago
    SET ESTADO_PAG = nuevo_estado,
        RAZON_PAG = razon_pago
    WHERE ID_PAGO = pago_id;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `AsignarInquilinoACuarto` (IN `id_inquilino_param` INT, IN `id_cuarto_param` INT)  BEGIN
    -- Actualizar el ID del inquilino y el estado del cuarto
    UPDATE Cuartos
    SET ID_INQUILINO = id_inquilino_param,
        ESTADO_CUA = 'Al día'
    WHERE ID_CUARTO = id_cuarto_param;
   		-- Generar nuevas cuotas mensuales
        CALL GenerarCuotasMensuales();
		-- Actualizar estado de los cuartos
        CALL ActualizarEstadoCuartos();
        -- Actualizar datos de los inquilinos
        CALL ActualizarDatosInquilinos();     
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ConsultarAdministradorPorID` (IN `admin_id` INT)  BEGIN
    DECLARE num_pagos INT;
    DECLARE num_solicitudes INT;
    DECLARE num_cuartos_libres INT;

    -- Contar la cantidad de pagos en estado diferente de "Aprobado"
    SELECT COUNT(*)
    INTO num_pagos
    FROM Pago
    WHERE ESTADO_PAG != 'Aprobado' AND ESTADO_PAG != 'Retirado';

    -- Contar la cantidad de solicitudes
    SELECT COUNT(*)
    INTO num_solicitudes
    FROM Solicitudes
    WHERE ESTADO_SOL IS NOT NULL; -- Opcional: Puedes agregar alguna condición adicional para filtrar las solicitudes según su estado

    -- Contar la cantidad de cuartos con estado "Libre"
    SELECT COUNT(*)
    INTO num_cuartos_libres
    FROM Cuartos
    WHERE ESTADO_CUA = 'Libre';

    -- Devolver los resultados
    SELECT *,
           num_pagos AS cantidad_pagos,
           num_solicitudes AS cantidad_solicitudes,
           num_cuartos_libres AS cantidad_cuartos_libres
    FROM Administradores
    WHERE ID_ADMINISTRADOR = admin_id;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ConsultarCuartoPorCodigo` (IN `codigo_cuarto` VARCHAR(3))  BEGIN
    SELECT 
        C.ID_CUARTO,
        C.ID_INQUILINO,
        C.CODIGO_CUA,
        C.TAMANO_CUA,
        C.PISO_CUA,
        C.COSTO_CUA,
        C.ESTADO_CUA,
        C.GARANTIA_CUA,
        I.DNI_INQ,
        I.NOMBRE_INQ,
        I.TELEFONO_INQ,
        I.FECHAINGRESO_INQ
    FROM Cuartos C
    LEFT JOIN Inquilinos I ON C.ID_INQUILINO = I.ID_INQUILINO
    WHERE C.CODIGO_CUA = codigo_cuarto;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ConsultarDatosCuotaConPagos` (IN `cuarto_id` INT)  BEGIN
    SELECT DISTINCT
        CU.ID_CUOTA,
        P.ID_PAGO,
        P.FECHA_PAG,
        P.MONTO_PAG,
        P.ESTADO_PAG,
        C.ID_CUARTO,
        C.CODIGO_CUA,
        C.PISO_CUA,
        C.COSTO_CUA,
        C.ESTADO_CUA,
        CU.FECHA_VENCIMIENTO,
        CU.ESTADO_CUO,
        CU.MONTO_CUO,
        I.ID_INQUILINO,
        I.NOMBRE_INQ
    FROM
        Cuotas CU
    LEFT JOIN
        Pago P ON CU.ID_CUOTA = P.ID_CUOTA
    INNER JOIN
        Cuartos C ON CU.ID_CUARTO = C.ID_CUARTO
    INNER JOIN
        Inquilinos I ON C.ID_INQUILINO = I.ID_INQUILINO
    WHERE
        C.ID_CUARTO = cuarto_id
        AND CU.ESTADO_CUO != 'Pagado' 
        AND CU.ESTADO_CUO != 'Retirado';
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ConsultarDatosCuotaConPagosPorCuartoPagados` (IN `cuarto_id` INT)  BEGIN
    SELECT
        P.ID_PAGO,
        P.ID_CUOTA,
        P.FECHA_PAG,
        P.MONTO_PAG,
        P.ESTADO_PAG,
        C.ID_CUARTO,
        C.CODIGO_CUA,
        C.PISO_CUA,
        C.COSTO_CUA,
        C.ESTADO_CUA,
        CU.FECHA_VENCIMIENTO,
        CU.ESTADO_CUO,
        CU.MONTO_CUO,
        I.ID_INQUILINO,
        I.NOMBRE_INQ
    FROM
        Cuartos C
    INNER JOIN
        Inquilinos I ON C.ID_INQUILINO = I.ID_INQUILINO
    INNER JOIN
        Cuotas CU ON CU.ID_CUARTO = C.ID_CUARTO
    LEFT JOIN
        (SELECT
            P1.ID_PAGO,
            P1.ID_CUOTA,
            P1.FECHA_PAG,
            P1.MONTO_PAG,
            P1.ESTADO_PAG
        FROM
            Pago P1
        INNER JOIN
            (SELECT
                ID_CUOTA,
                MAX(FECHA_PAG) AS UltimaFechaPago
            FROM
                Pago
            GROUP BY
                ID_CUOTA) UltimosPagos ON P1.ID_CUOTA = UltimosPagos.ID_CUOTA AND P1.FECHA_PAG = UltimosPagos.UltimaFechaPago) P ON CU.ID_CUOTA = P.ID_CUOTA
    WHERE
        C.ID_CUARTO = cuarto_id
        AND CU.ESTADO_CUO = 'Pagado'
        AND CU.ESTADO_CUO != 'Retirado';
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ConsultarDatosPago` (IN `codigo_cuarto` VARCHAR(50))  BEGIN
    SELECT P.ID_PAGO, P.ID_CUOTA, I.ID_INQUILINO, P.FECHA_PAG, P.MONTO_PAG, P.ESTADO_PAG,
           C.CODIGO_CUA, C.PISO_CUA, C.COSTO_CUA, C.ESTADO_CUA,
           CU.FECHA_VENCIMIENTO, CU.ESTADO_CUO, CU.MONTO_CUO,
           I.NOMBRE_INQ
    FROM Pago P
    INNER JOIN Cuotas CU ON P.ID_CUOTA = CU.ID_CUOTA
    INNER JOIN Cuartos C ON CU.ID_CUARTO = C.ID_CUARTO
    INNER JOIN Inquilinos I ON C.ID_INQUILINO = I.ID_INQUILINO
    WHERE C.CODIGO_CUA = codigo_cuarto
    AND P.ESTADO_PAG <> 'Aprobado'
	AND P.ESTADO_PAG <> 'Rechazado'; -- Excluir pagos con estado 'Aprobado'
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ConsultarTodosCuartos` ()  BEGIN
    SELECT 
        C.ID_CUARTO,
        C.ID_INQUILINO,
        C.CODIGO_CUA,
        C.TAMANO_CUA,
        C.PISO_CUA,
        C.COSTO_CUA,
        C.ESTADO_CUA,
        I.DNI_INQ,
        I.NOMBRE_INQ,
        I.TELEFONO_INQ,
        I.FECHAINGRESO_INQ
    FROM Cuartos C
    LEFT JOIN Inquilinos I ON C.ID_INQUILINO = I.ID_INQUILINO
WHERE C.ESTADO_CUA <> 'Libre';
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ConsultarTodosCuartosLibres` ()  BEGIN
    SELECT 
        C.ID_CUARTO,
        C.ID_INQUILINO,
        C.CODIGO_CUA,
        C.TAMANO_CUA,
        C.PISO_CUA,
        C.COSTO_CUA,
        C.ESTADO_CUA,
        I.DNI_INQ,
        I.NOMBRE_INQ,
        I.TELEFONO_INQ,
        I.FECHAINGRESO_INQ
    FROM Cuartos C
    LEFT JOIN Inquilinos I ON C.ID_INQUILINO = I.ID_INQUILINO
WHERE C.ESTADO_CUA = 'Libre';
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `EditarInquilino` (IN `p_id_inquilino` INT, IN `p_dni` VARCHAR(8), IN `p_nombre` VARCHAR(50), IN `p_telefono` VARCHAR(11), IN `p_fechaingreso` DATE)  BEGIN
    -- Actualizar los campos del inquilino
    UPDATE Inquilinos
    SET DNI_INQ = p_dni,
        NOMBRE_INQ = p_nombre,
        TELEFONO_INQ = p_telefono,
        FECHAINGRESO_INQ = p_fechaingreso
    WHERE ID_INQUILINO = p_id_inquilino;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `GenerarCuotasMensuales` ()  BEGIN
    DECLARE done BOOLEAN DEFAULT FALSE;
    DECLARE inquilino_id INT;
    DECLARE fecha_ingreso DATE;
    DECLARE fecha_cuota DATE;
    DECLARE cuota_existente INT;
    DECLARE cuotas_generadas INT DEFAULT 0;

    -- Cursor para seleccionar todos los inquilinos
    DECLARE cur CURSOR FOR SELECT ID_INQUILINO, FECHAINGRESO_INQ FROM Inquilinos;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO inquilino_id, fecha_ingreso;
        IF done THEN
            LEAVE read_loop;
        END IF;

        -- Establecer la fecha de la primera cuota como la fecha de ingreso del inquilino
        SET fecha_cuota = fecha_ingreso;

        -- Generar cuotas hasta la fecha actual
        WHILE fecha_cuota <= CURDATE() DO
            -- Verificar si ya existe una cuota para el inquilino en la fecha de vencimiento
            SELECT COUNT(*) INTO cuota_existente
            FROM Cuotas
            WHERE ID_CUARTO IN (SELECT ID_CUARTO FROM Cuartos WHERE ID_INQUILINO = inquilino_id)
            AND FECHA_VENCIMIENTO = fecha_cuota;

            -- Si no hay cuota existente, insertar una nueva cuota
            IF cuota_existente = 0 THEN
                INSERT INTO Cuotas (ID_CUARTO, FECHA_VENCIMIENTO, ESTADO_CUO)
                SELECT ID_CUARTO, fecha_cuota, 'Pendiente'
                FROM Cuartos
                WHERE ID_INQUILINO = inquilino_id;
            END IF;

            -- Avanzar al mismo día del siguiente mes
            SET fecha_cuota = DATE_ADD(fecha_cuota, INTERVAL 1 MONTH);
        END WHILE;

        -- Reiniciar la fecha_cuota para las cuotas futuras
        SET cuotas_generadas = 0;

        -- Generar tres cuotas futuras
        WHILE cuotas_generadas < 3 DO
            -- Verificar si ya existe una cuota para el inquilino en la fecha de vencimiento
            SELECT COUNT(*) INTO cuota_existente
            FROM Cuotas
            WHERE ID_CUARTO IN (SELECT ID_CUARTO FROM Cuartos WHERE ID_INQUILINO = inquilino_id)
            AND FECHA_VENCIMIENTO = fecha_cuota;

            -- Si no hay cuota existente, insertar una nueva cuota
            IF cuota_existente = 0 THEN
                INSERT INTO Cuotas (ID_CUARTO, FECHA_VENCIMIENTO, ESTADO_CUO)
                SELECT ID_CUARTO, fecha_cuota, 'Futuro'
                FROM Cuartos
                WHERE ID_INQUILINO = inquilino_id;

                -- Incrementar contador de cuotas generadas
                SET cuotas_generadas = cuotas_generadas + 1;
            END IF;

            -- Avanzar al mismo día del siguiente mes
            SET fecha_cuota = DATE_ADD(fecha_cuota, INTERVAL 1 MONTH);
        END WHILE;
    END LOOP;

    CLOSE cur;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `InsertarNuevoInquilino` (IN `dni_param` VARCHAR(8), IN `nombre_param` VARCHAR(50), IN `contrasena_param` VARCHAR(25), IN `nomusuario_param` VARCHAR(25), IN `telefono_param` VARCHAR(11), IN `fechaingreso_param` DATE, IN `pagos_param` INT, IN `deudas_param` INT, IN `solicitudes_param` INT)  BEGIN
    DECLARE usuario_existente INT;
    
    -- Verificar si el nombre de usuario ya existe
    SELECT COUNT(*) INTO usuario_existente FROM Inquilinos WHERE NOMUSUARIO_INQ = nomusuario_param;
    
    -- Si el nombre de usuario ya existe, devolver un mensaje de error
    IF usuario_existente > 0 THEN
        SELECT -1 AS ID_Inquilino, 'El nombre de usuario ya existe' AS Resultado;
    ELSE
        -- Insertar el nuevo usuario y devolver el ID
        INSERT INTO Inquilinos (DNI_INQ, NOMBRE_INQ, CONTRASENA_INQ, NOMUSUARIO_INQ, TELEFONO_INQ, FECHAINGRESO_INQ, PAGOS_INQ, DEUDAS_INQ, SOLICITUDES_INQ)
        VALUES (dni_param, nombre_param, contrasena_param, nomusuario_param, telefono_param, fechaingreso_param, pagos_param, deudas_param, solicitudes_param);
        
        -- Devolver el ID del nuevo usuario
        SELECT LAST_INSERT_ID() AS ID_Inquilino, 'Nuevo usuario insertado correctamente' AS Resultado;
    END IF;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `InsertarPago` (IN `id_cuota_param` INT, IN `fecha_pag_param` DATE, IN `monto_pag_param` INT, IN `estado_pag_param` VARCHAR(15), IN `razon_pag_param` VARCHAR(150))  BEGIN
    DECLARE rows_affected INT;

    -- Insertar el nuevo pago en la tabla Pago
    INSERT INTO Pago (ID_CUOTA, FECHA_PAG, MONTO_PAG, ESTADO_PAG, RAZON_PAG)
    VALUES (id_cuota_param, fecha_pag_param, monto_pag_param, estado_pag_param, razon_pag_param);

    -- Mensaje de depuración
    SELECT 'Pago insertado' AS message;

    -- Verificar parámetros de entrada
    SELECT id_cuota_param AS id_cuota_param, fecha_pag_param AS fecha_pag_param;

    -- Actualizar el estado de la cuota a 'Pagado' si el estado del pago es 'Aprobado'
    IF estado_pag_param = 'Aprobado' THEN
        UPDATE Cuotas
        SET ESTADO_CUO = 'Pagado'
        WHERE ID_CUOTA = id_cuota_param;

        -- Obtener el número de filas afectadas por la actualización
        SET rows_affected = ROW_COUNT();

        -- Mensaje de depuración
        SELECT rows_affected AS filas_actualizadas;

        -- Mensaje de confirmación
        IF rows_affected > 0 THEN
            SELECT 'Cuota actualizada a Pagado' AS message;
        ELSE
            SELECT 'No se encontró cuota para actualizar' AS message;
        END IF;
    ELSE
        -- Mensaje de depuración si el estado no es 'Aprobado'
        SELECT 'Estado del pago no es Aprobado' AS message;
    END IF;

    -- Llamar a los procedimientos para actualizar datos de inquilinos y estado de cuartos
    CALL ActualizarDatosInquilinos();
    CALL ActualizarEstadoCuartos();
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `InsertarPagocFoto` (IN `p_ID_CUOTA` INT, IN `p_FECHA_PAG` DATE, IN `p_MONTO_PAG` INT, IN `p_FOTO_PAG` LONGBLOB, IN `p_RUTA_FOTO` VARCHAR(150), IN `p_ESTADO_PAG` VARCHAR(15), IN `p_RAZON_PAGO` VARCHAR(150))  BEGIN
    INSERT INTO Pago (ID_CUOTA, FECHA_PAG, MONTO_PAG, FOTO_PAG, RUTA_FOTO, ESTADO_PAG, RAZON_PAG)
    VALUES (p_ID_CUOTA, p_FECHA_PAG, p_MONTO_PAG, p_FOTO_PAG, p_RUTA_FOTO, p_ESTADO_PAG, p_RAZON_PAGO);
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `InsertarSolicitud` (IN `p_id_inquilino` INT, IN `p_id_administrador` INT, IN `p_nombre_sol` VARCHAR(255), IN `p_descripcion_sol` TEXT, IN `p_fecha_sol` DATE, IN `p_estado_sol` VARCHAR(50), IN `p_monto_sol` DECIMAL(10,2))  BEGIN
    INSERT INTO Solicitudes (
        ID_INQUILINO, 
        ID_ADMINISTRADOR, 
        NOMBRE_SOL, 
        DESCRIPCION_SOL, 
        FECHA_SOL, 
        ESTADO_SOL, 
        MONTO_SOL
    ) VALUES (
        p_id_inquilino, 
        p_id_administrador, 
        p_nombre_sol, 
        p_descripcion_sol, 
        p_fecha_sol, 
        p_estado_sol, 
        p_monto_sol
    );
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `LiberarCuarto` (IN `p_id_cuarto` INT)  BEGIN
    DECLARE deudas_pendientes INT;

    -- Verificar si hay deudas pendientes para el cuarto
    SELECT COUNT(*) INTO deudas_pendientes
    FROM Cuartos C
    INNER JOIN Inquilinos I ON C.ID_INQUILINO = I.ID_INQUILINO
    WHERE C.ID_CUARTO = p_id_cuarto AND I.DEUDAS_INQ > 0;

    -- Si no hay deudas pendientes, cambiar el estado del cuarto a "Libre", del pago a "Retirado" y de la cuota a "Retirado"
    IF deudas_pendientes = 0 THEN
        UPDATE Cuartos
        SET ID_INQUILINO = NULL,
        ESTADO_CUA = 'Libre'
        WHERE ID_CUARTO = p_id_cuarto;
        
        UPDATE Pago
        SET ESTADO_PAG = 'Retirado',
            RAZON_PAG = 'Pago pero usuario se retiró'
        WHERE ID_CUOTA IN (SELECT ID_CUOTA FROM Cuotas WHERE ID_CUARTO = p_id_cuarto);
        
        UPDATE Cuotas
        SET ESTADO_CUO = 'Retirado'
        WHERE ID_CUARTO = p_id_cuarto;
        
        SELECT 'El cuarto ha sido liberado correctamente, el pago retirado y la cuota retirada.' AS mensaje;
    ELSE
        SELECT 'No se puede liberar el cuarto debido a que hay deudas pendientes.' AS mensaje;
    END IF;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ObtenerDatosInquilinoYCuarto` (IN `p_id_inquilino` INT)  BEGIN
    DECLARE v_nombre_inquilino VARCHAR(50);
    DECLARE v_dni_inquilino VARCHAR(8);
    DECLARE v_telefono_inquilino VARCHAR(11);
    DECLARE v_fecha_ingreso_inquilino DATE;
    DECLARE v_pagos_inquilino INT;
    DECLARE v_deudas_inquilino INT;
    DECLARE v_solicitudes_inquilino INT;
    DECLARE v_id_cuarto INT;
    DECLARE v_codigo_cuarto VARCHAR(3);
    DECLARE v_tamano_cuarto VARCHAR(3);
    DECLARE v_piso_cuarto VARCHAR(3);
    DECLARE v_costo_cuarto INT;
    DECLARE v_estado_cuarto VARCHAR(20);
    DECLARE v_costo_garantia INT;
    
    -- Obtener los datos del inquilino
    SELECT NOMBRE_INQ, DNI_INQ, TELEFONO_INQ, FECHAINGRESO_INQ, PAGOS_INQ, DEUDAS_INQ, SOLICITUDES_INQ
    INTO v_nombre_inquilino, v_dni_inquilino, v_telefono_inquilino, v_fecha_ingreso_inquilino, v_pagos_inquilino, v_deudas_inquilino, v_solicitudes_inquilino
    FROM Inquilinos
    WHERE ID_INQUILINO = p_id_inquilino;
    
    -- Obtener los datos del cuarto del inquilino
    SELECT ID_CUARTO, CODIGO_CUA, TAMANO_CUA, PISO_CUA, COSTO_CUA, ESTADO_CUA, GARANTIA_CUA
    INTO v_id_cuarto, v_codigo_cuarto, v_tamano_cuarto, v_piso_cuarto, v_costo_cuarto, v_estado_cuarto, v_costo_garantia
    FROM Cuartos
    WHERE ID_INQUILINO = p_id_inquilino;
    
    -- Devolver los resultados
    SELECT 
        v_nombre_inquilino AS Nombre_Inquilino, 
        v_dni_inquilino AS DNI_Inquilino, 
        v_telefono_inquilino AS Telefono_Inquilino, 
        v_fecha_ingreso_inquilino AS Fecha_Ingreso_Inquilino, 
        v_pagos_inquilino AS Pagos_Inquilino, 
        v_deudas_inquilino AS Deudas_Inquilino, 
        v_solicitudes_inquilino AS Solicitudes_Inquilino,
        v_id_cuarto AS ID_Cuarto,
        v_codigo_cuarto AS Codigo_Cuarto,
        v_tamano_cuarto AS Tamano_Cuarto,
        v_piso_cuarto AS Piso_Cuarto,
        v_costo_cuarto AS Costo_Cuarto,
        v_estado_cuarto AS Estado_Cuarto,
        v_costo_garantia AS Costo_Garantia;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ObtenerFotoPago` (IN `p_pago_id` INT)  BEGIN
    DECLARE foto_pago LONGBLOB;
	DECLARE url_foto VARCHAR(550);
    -- Recuperar la foto del pago y la URL
    SELECT FOTO_PAG, RUTA_FOTO INTO foto_pago, url_foto
    FROM Pago
    WHERE ID_PAGO = p_pago_id;

    -- Devolver la foto del pago y la URL
    SELECT foto_pago AS FOTO_PAG, url_foto AS RUTA_FOTO;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ValidarAdminUsuarioContrasena` (IN `p_usuario` VARCHAR(25), IN `p_contrasena` VARCHAR(25), OUT `p_valido` INT, OUT `p_ID_Usuario` INT)  BEGIN
    DECLARE admin_id INT;

    -- Inicializa el resultado en 0 (sin éxito)
    SET p_valido = 0;

    -- Busca un registro en la tabla Administradores que coincida con el usuario y la contraseña
    SELECT ID_ADMINISTRADOR INTO admin_id
    FROM Administradores
    WHERE NOMUSUARIO_ADM = p_usuario AND CONTRASENA_ADM = p_contrasena;

    -- Si se encuentra un registro, la validación es exitosa y se establece el resultado en 1
    IF admin_id IS NOT NULL THEN
        SET p_valido = 1;
        SET p_ID_Usuario = admin_id;
    END IF;
END$$

CREATE DEFINER=`admin`@`localhost` PROCEDURE `ValidarInquilinoUsuarioContrasena` (IN `p_usuario` VARCHAR(25), IN `p_contrasena` VARCHAR(25), OUT `p_valido` INT, OUT `p_ID_Inquilino` INT)  BEGIN
    DECLARE inquilino_id INT;

    -- Inicializa el resultado en 0 (sin éxito)
    SET p_valido = 0;

    -- Busca un registro en la tabla Inquilinos que coincida con el usuario y la contraseña
    SELECT ID_INQUILINO INTO inquilino_id
    FROM Inquilinos
    WHERE NOMUSUARIO_INQ = p_usuario AND CONTRASENA_INQ = p_contrasena;

    -- Si se encuentra un registro, la validación es exitosa y se establece el resultado en 1
    IF inquilino_id IS NOT NULL THEN
        SET p_valido = 1;
        SET p_ID_Inquilino = inquilino_id;
    END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Administradores`
--

CREATE TABLE `Administradores` (
  `ID_ADMINISTRADOR` int NOT NULL,
  `DNI_ADM` varchar(8) DEFAULT NULL,
  `NOMBRE_ADM` varchar(50) DEFAULT NULL,
  `APELLIDOS_ADM` varchar(50) DEFAULT NULL,
  `CONTRASENA_ADM` varchar(25) DEFAULT NULL,
  `NOMUSUARIO_ADM` varchar(25) DEFAULT NULL,
  `CORREO_ADM` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `TIPO_ADM` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Administradores`
--

INSERT INTO `Administradores` (`ID_ADMINISTRADOR`, `DNI_ADM`, `NOMBRE_ADM`, `APELLIDOS_ADM`, `CONTRASENA_ADM`, `NOMUSUARIO_ADM`, `CORREO_ADM`, `TIPO_ADM`) VALUES
(1, '75247254', 'Danay ', ' Vilcahuaman Paucar', '75247254@Danay', 'DanaV75247254', 'dadevipa@gmail.com', 'Administrador'),
(2, '41080104', 'Pedro', 'Del Rio Calderon', 'pedro@41080104', 'PedroD41080104', 'ptrujillo_c@hotmail.com', 'Administrador');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Cuartos`
--

CREATE TABLE `Cuartos` (
  `ID_CUARTO` int NOT NULL,
  `ID_INQUILINO` int DEFAULT NULL,
  `CODIGO_CUA` varchar(3) DEFAULT NULL,
  `TAMANO_CUA` varchar(3) DEFAULT NULL,
  `PISO_CUA` varchar(3) DEFAULT NULL,
  `COSTO_CUA` int DEFAULT NULL,
  `ESTADO_CUA` varchar(20) DEFAULT NULL,
  `GARANTIA_CUA` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Cuartos`
--

INSERT INTO `Cuartos` (`ID_CUARTO`, `ID_INQUILINO`, `CODIGO_CUA`, `TAMANO_CUA`, `PISO_CUA`, `COSTO_CUA`, `ESTADO_CUA`, `GARANTIA_CUA`) VALUES
(2, 1, 'MKO', '0', '201', 500, 'Al Día', 1000),
(3, NULL, 'MKO', '0', '202', 0, 'Libre', 0),
(4, NULL, 'MKO', '0', '203', 0, 'Libre', 0),
(5, NULL, 'MKO', '0', '204', 0, 'Libre', 0),
(6, NULL, 'MKO', '0', '205', 0, 'Libre', 0),
(7, NULL, 'MKO', '0', '206', 0, 'Libre', 0),
(8, NULL, 'MKO', '0', '207', 0, 'Libre', 0),
(9, NULL, 'MKO', '0', '208', 0, 'Libre', 0),
(10, NULL, 'MKO', '0', '209', 0, 'Libre', 0),
(11, NULL, 'MKO', '0', '210', 0, 'Libre', 0),
(12, NULL, 'MKO', '0', '211', 0, 'Libre', 0),
(13, NULL, 'MKO', '0', '212', 0, 'Libre', 0),
(14, NULL, 'MKO', '0', '213', 0, 'Libre', 0),
(15, NULL, 'MKO', '0', '214', 0, 'Libre', 0),
(16, NULL, 'MKO', '0', '215', 0, 'Libre', 0),
(17, NULL, 'MKO', '0', '301', 0, 'Libre', 0),
(18, NULL, 'MKO', '0', '302', 0, 'Libre', 0),
(19, NULL, 'MKO', '0', '303', 0, 'Libre', 0),
(20, NULL, 'MKO', '0', '304', 0, 'Libre', 0),
(21, NULL, 'MKO', '0', '305', 0, 'Libre', 0),
(22, NULL, 'MKO', '0', '306', 0, 'Libre', 0),
(23, NULL, 'MKO', '0', '307', 0, 'Libre', 0),
(24, NULL, 'MKO', '0', '308', 0, 'Libre', 0),
(25, NULL, 'MKO', '0', '309', 0, 'Libre', 0),
(26, NULL, 'MKO', '0', '310', 0, 'Libre', 0),
(27, NULL, 'MKO', '0', '311', 0, 'Libre', 0),
(28, NULL, 'MKO', '0', '312', 0, 'Libre', 0),
(29, NULL, 'MKO', '0', '313', 0, 'Libre', 0),
(30, NULL, 'MKO', '0', '314', 0, 'Libre', 0),
(31, NULL, 'MKO', '0', '315', 0, 'Libre', 0),
(32, NULL, 'MKO', '0', '401', 0, 'Libre', 0),
(33, NULL, 'MKO', '0', '402', 0, 'Libre', 0),
(34, NULL, 'MKO', '0', '403', 0, 'Libre', 0),
(35, NULL, 'MKO', '0', '404', 0, 'Libre', 0),
(36, NULL, 'MKO', '0', '405', 0, 'Libre', 0),
(37, NULL, 'MKO', '0', '406', 0, 'Libre', 0),
(38, NULL, 'MKO', '0', '407', 0, 'Libre', 0),
(39, NULL, 'MKO', '0', '408', 0, 'Libre', 0),
(40, NULL, 'MKO', '0', '409', 0, 'Libre', 0),
(41, NULL, 'MKO', '0', '410', 0, 'Libre', 0),
(42, NULL, 'MKO', '0', '411', 0, 'Libre', 0),
(43, NULL, 'MKO', '0', '412', 0, 'Libre', 0),
(44, NULL, 'MKO', '0', '413', 0, 'Libre', 0),
(45, NULL, 'MKO', '0', '414', 0, 'Libre', 0),
(46, NULL, 'MKO', '0', '415', 0, 'Libre', 0),
(47, NULL, 'MKO', '0', '501', 0, 'Libre', 0),
(48, NULL, 'MKO', '0', '502', 0, 'Libre', 0),
(49, NULL, 'MKO', '0', '503', 0, 'Libre', 0),
(50, NULL, 'MKO', '0', '504', 0, 'Libre', 0),
(51, NULL, 'MKO', '0', '505', 0, 'Libre', 0),
(52, NULL, 'MKO', '0', '506', 0, 'Libre', 0),
(53, NULL, 'MKO', '0', '507', 0, 'Libre', 0),
(54, NULL, 'MKO', '0', '508', 0, 'Libre', 0),
(55, NULL, 'MKO', '0', '509', 0, 'Libre', 0),
(56, NULL, 'MKO', '0', '510', 0, 'Libre', 0),
(57, NULL, 'MKO', '0', '511', 0, 'Libre', 0),
(58, NULL, 'MKO', '0', '512', 0, 'Libre', 0),
(59, NULL, 'MKO', '0', '513', 0, 'Libre', 0),
(60, NULL, 'MKO', '0', '514', 0, 'Libre', 0),
(61, NULL, 'MKO', '0', '515', 0, 'Libre', 0),
(62, NULL, 'MKO', '0', '601', 0, 'Libre', 0),
(63, NULL, 'MKO', '0', '602', 0, 'Libre', 0),
(64, NULL, 'MKO', '0', '603', 0, 'Libre', 0),
(65, NULL, 'MKO', '0', '604', 0, 'Libre', 0),
(66, NULL, 'MKO', '0', '605', 0, 'Libre', 0),
(67, NULL, 'MKO', '0', '606', 0, 'Libre', 0),
(68, NULL, 'MKO', '0', '607', 0, 'Libre', 0),
(69, NULL, 'MKO', '0', '608', 0, 'Libre', 0),
(70, NULL, 'MKO', '0', '609', 0, 'Libre', 0),
(71, NULL, 'MKO', '0', '610', 0, 'Libre', 0),
(72, NULL, 'MKO', '0', '611', 0, 'Libre', 0),
(73, NULL, 'MKO', '0', '612', 0, 'Libre', 0),
(74, NULL, 'MKO', '0', '613', 0, 'Libre', 0),
(75, NULL, 'MKO', '0', '614', 0, 'Libre', 0),
(76, NULL, 'MKO', '0', '615', 0, 'Libre', 0),
(77, NULL, 'MKO', '0', '701', 0, 'Libre', 0),
(78, NULL, 'MKO', '0', '702', 0, 'Libre', 0),
(79, NULL, 'MKO', '0', '703', 0, 'Libre', 0),
(80, NULL, 'MKO', '0', '704', 0, 'Libre', 0),
(81, NULL, 'MKO', '0', '705', 0, 'Libre', 0),
(82, NULL, 'MKO', '0', '706', 0, 'Libre', 0),
(83, NULL, 'MKO', '0', '707', 0, 'Libre', 0),
(84, NULL, 'MKO', '0', '708', 0, 'Libre', 0),
(85, NULL, 'MKO', '0', '709', 0, 'Libre', 0),
(86, NULL, 'MKO', '0', '710', 0, 'Libre', 0),
(87, NULL, 'MKO', '0', '711', 0, 'Libre', 0),
(88, NULL, 'MKO', '0', '712', 0, 'Libre', 0),
(89, NULL, 'MKO', '0', '713', 0, 'Libre', 0),
(90, NULL, 'MKO', '0', '714', 0, 'Libre', 0),
(91, NULL, 'MKO', '0', '715', 0, 'Libre', 0),
(92, NULL, 'PID', '0', '201', 0, 'Libre', 0),
(93, NULL, 'PID', '0', '301', 0, 'Libre', 0),
(94, NULL, 'PID', '0', '302', 0, 'Libre', 0),
(95, NULL, 'PID', '0', '303', 0, 'Libre', 0),
(96, NULL, 'PID', '0', '304', 0, 'Libre', 0),
(97, NULL, 'PID', '0', '305', 0, 'Libre', 0),
(98, NULL, 'PID', '0', '306', 0, 'Libre', 0),
(99, NULL, 'PID', '0', '307', 0, 'Libre', 0),
(100, NULL, 'PID', '0', '401', 0, 'Libre', 0),
(101, NULL, 'PID', '0', '402', 0, 'Libre', 0),
(102, NULL, 'PID', '0', '403', 0, 'Libre', 0),
(103, NULL, 'PID', '0', '404', 0, 'Libre', 0),
(104, NULL, 'PID', '0', '405', 0, 'Libre', 0),
(105, NULL, 'PID', '0', '406', 0, 'Libre', 0),
(106, NULL, 'PID', '0', '407', 0, 'Libre', 0),
(107, NULL, 'PID', '0', '501', 0, 'Libre', 0),
(108, NULL, 'PID', '0', '502', 0, 'Libre', 0),
(109, NULL, 'PID', '0', '503', 0, 'Libre', 0),
(110, NULL, 'PID', '0', '504', 0, 'Libre', 0),
(111, NULL, 'PID', '0', '505', 0, 'Libre', 0),
(112, NULL, 'PID', '0', '506', 0, 'Libre', 0),
(113, NULL, 'PID', '0', '507', 0, 'Libre', 0),
(114, NULL, 'PID', '0', '601', 0, 'Libre', 0),
(115, NULL, 'PID', '0', '602', 0, 'Libre', 0),
(116, NULL, 'PID', '0', '603', 0, 'Libre', 0),
(117, NULL, 'PID', '0', '604', 0, 'Libre', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Cuotas`
--

CREATE TABLE `Cuotas` (
  `ID_CUOTA` int NOT NULL,
  `ID_CUARTO` int DEFAULT NULL,
  `FECHA_VENCIMIENTO` date DEFAULT NULL,
  `ESTADO_CUO` varchar(10) DEFAULT NULL,
  `MONTO_CUO` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Cuotas`
--

INSERT INTO `Cuotas` (`ID_CUOTA`, `ID_CUARTO`, `FECHA_VENCIMIENTO`, `ESTADO_CUO`, `MONTO_CUO`) VALUES
(1, 2, '2024-07-20', 'Pagado', 250),
(2, 2, '2024-08-20', 'Futuro', 250),
(3, 2, '2024-09-20', 'Futuro', 250),
(4, 2, '2024-10-20', 'Futuro', 250);

--
-- Disparadores `Cuotas`
--
DELIMITER $$
CREATE TRIGGER `calcular_monto_cuota` BEFORE INSERT ON `Cuotas` FOR EACH ROW BEGIN
    DECLARE costo_cuarto INT;
    
    -- Obtener el costo del cuarto asociado a la cuota
    SELECT COSTO_CUA INTO costo_cuarto
    FROM Cuartos
    WHERE ID_CUARTO = NEW.ID_CUARTO;
    
    -- Calcular el monto de la cuota basado en el costo del cuarto
    SET NEW.MONTO_CUO = costo_cuarto;  -- Puedes ajustar la fórmula de cálculo según tus necesidades
    
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Inquilinos`
--

CREATE TABLE `Inquilinos` (
  `ID_INQUILINO` int NOT NULL,
  `DNI_INQ` varchar(8) DEFAULT NULL,
  `NOMBRE_INQ` varchar(50) DEFAULT NULL,
  `CONTRASENA_INQ` varchar(25) DEFAULT NULL,
  `NOMUSUARIO_INQ` varchar(25) DEFAULT NULL,
  `TELEFONO_INQ` varchar(11) DEFAULT NULL,
  `FECHAINGRESO_INQ` date DEFAULT NULL,
  `PAGOS_INQ` int DEFAULT NULL,
  `DEUDAS_INQ` int DEFAULT NULL,
  `SOLICITUDES_INQ` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Inquilinos`
--

INSERT INTO `Inquilinos` (`ID_INQUILINO`, `DNI_INQ`, `NOMBRE_INQ`, `CONTRASENA_INQ`, `NOMUSUARIO_INQ`, `TELEFONO_INQ`, `FECHAINGRESO_INQ`, `PAGOS_INQ`, `DEUDAS_INQ`, `SOLICITUDES_INQ`) VALUES
(1, '70088117', 'max', '123', 'max', '963836916', '2024-07-20', 4, 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Pago`
--

CREATE TABLE `Pago` (
  `ID_PAGO` int NOT NULL,
  `ID_CUOTA` int DEFAULT NULL,
  `FECHA_PAG` date DEFAULT NULL,
  `MONTO_PAG` int DEFAULT NULL,
  `FOTO_PAG` longblob,
  `RUTA_FOTO` varchar(150) DEFAULT NULL,
  `ESTADO_PAG` varchar(15) DEFAULT NULL,
  `RAZON_PAG` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `Pago`
--

INSERT INTO `Pago` (`ID_PAGO`, `ID_CUOTA`, `FECHA_PAG`, `MONTO_PAG`, `FOTO_PAG`, `RUTA_FOTO`, `ESTADO_PAG`, `RAZON_PAG`) VALUES
(1, 1, '2024-07-20', 250, '', '669beb82d64e9.jpg', 'Rechazado', 'Foto Inadecuada'),
(2, 1, '2024-07-20', 500, NULL, NULL, 'Aprobado', 'Pago en Licoreria por: '),
(3, 2, '2024-07-20', 500, NULL, NULL, 'Aprobado', 'Pago en Licoreria por: '),
(4, 3, '2024-07-22', 250, '', '669e6cfa09b67.jpg', 'Revisando', 'Reenvio de Foto');

--
-- Disparadores `Pago`
--
DELIMITER $$
CREATE TRIGGER `DespuesdeAprobarPago` AFTER UPDATE ON `Pago` FOR EACH ROW BEGIN
    IF NEW.ESTADO_PAG = 'Aprobado' THEN
    	UPDATE Cuotas
        SET ESTADO_CUO = 'Pagado'
        WHERE ID_CUOTA = NEW.ID_CUOTA;

        -- Actualizar datos de los inquilinos
        CALL ActualizarDatosInquilinos();

        -- Actualizar estado de los cuartos
        CALL ActualizarEstadoCuartos();
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Solicitudes`
--

CREATE TABLE `Solicitudes` (
  `ID_SOLICITUD` int NOT NULL,
  `ID_INQUILINO` int DEFAULT NULL,
  `ID_ADMINISTRADOR` int DEFAULT NULL,
  `NOMBRE_SOL` varchar(50) DEFAULT NULL,
  `DESCRIPCION_SOL` varchar(250) DEFAULT NULL,
  `FECHA_SOL` date DEFAULT NULL,
  `ESTADO_SOL` varchar(11) DEFAULT NULL,
  `MONTO_SOL` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Disparadores `Solicitudes`
--
DELIMITER $$
CREATE TRIGGER `actualizar_cuotas_despues_solicitud` AFTER INSERT ON `Solicitudes` FOR EACH ROW BEGIN 
    DECLARE ultima_cuota_pagada INT; 
    DECLARE siguiente_cuota INT; 
    DECLARE inquilino_id INT; 
    DECLARE monto_solicitud INT; 
    DECLARE monto_restante INT; 

    -- Obtener el inquilino asociado a la solicitud 
    SELECT ID_INQUILINO INTO inquilino_id FROM Solicitudes WHERE ID_SOLICITUD = NEW.ID_SOLICITUD; 

    -- Obtener la última cuota pagada del inquilino 
    SELECT MAX(ID_CUOTA) INTO ultima_cuota_pagada FROM Cuotas 
    WHERE ID_CUARTO IN (SELECT ID_CUARTO FROM Cuartos WHERE ID_INQUILINO = inquilino_id) 
    AND ESTADO_CUO = 'Pagado';

    -- Obtener la siguiente cuota a la última pagada 
    SELECT MIN(ID_CUOTA) INTO siguiente_cuota FROM Cuotas 
    WHERE ID_CUARTO IN (SELECT ID_CUARTO FROM Cuartos WHERE ID_INQUILINO = inquilino_id) 
    AND ID_CUOTA > ultima_cuota_pagada 
    AND ESTADO_CUO != 'Pagado'
    LIMIT 1;

    -- Obtener el monto de la solicitud 
    SELECT MONTO_SOL INTO monto_solicitud FROM Solicitudes WHERE ID_SOLICITUD = NEW.ID_SOLICITUD; 

    -- Si la solicitud es de Descuento 
    IF NEW.NOMBRE_SOL = 'Descuento' THEN 
        -- Actualizar la cuota restando el monto de la solicitud 
        UPDATE Cuotas SET MONTO_CUO = CASE WHEN monto_solicitud >= MONTO_CUO THEN 0 ELSE MONTO_CUO - monto_solicitud END
        WHERE ID_CUOTA = siguiente_cuota;
    -- Si la solicitud es de Adicionado 
    ELSEIF NEW.NOMBRE_SOL = 'Adicionado' THEN 
        -- Actualizar la cuota sumando el monto de la solicitud 
        UPDATE Cuotas SET MONTO_CUO = MONTO_CUO + monto_solicitud WHERE ID_CUOTA = siguiente_cuota;
    END IF; 
END
$$
DELIMITER ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Administradores`
--
ALTER TABLE `Administradores`
  ADD PRIMARY KEY (`ID_ADMINISTRADOR`);

--
-- Indices de la tabla `Cuartos`
--
ALTER TABLE `Cuartos`
  ADD PRIMARY KEY (`ID_CUARTO`),
  ADD KEY `ID_INQUILINO` (`ID_INQUILINO`);

--
-- Indices de la tabla `Cuotas`
--
ALTER TABLE `Cuotas`
  ADD PRIMARY KEY (`ID_CUOTA`),
  ADD KEY `ID_CUARTO` (`ID_CUARTO`);

--
-- Indices de la tabla `Inquilinos`
--
ALTER TABLE `Inquilinos`
  ADD PRIMARY KEY (`ID_INQUILINO`);

--
-- Indices de la tabla `Pago`
--
ALTER TABLE `Pago`
  ADD PRIMARY KEY (`ID_PAGO`),
  ADD KEY `ID_CUOTA` (`ID_CUOTA`);

--
-- Indices de la tabla `Solicitudes`
--
ALTER TABLE `Solicitudes`
  ADD PRIMARY KEY (`ID_SOLICITUD`),
  ADD KEY `ID_INQUILINO` (`ID_INQUILINO`),
  ADD KEY `ID_ADMINISTRADOR` (`ID_ADMINISTRADOR`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `Administradores`
--
ALTER TABLE `Administradores`
  MODIFY `ID_ADMINISTRADOR` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `Cuartos`
--
ALTER TABLE `Cuartos`
  MODIFY `ID_CUARTO` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=118;

--
-- AUTO_INCREMENT de la tabla `Cuotas`
--
ALTER TABLE `Cuotas`
  MODIFY `ID_CUOTA` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `Inquilinos`
--
ALTER TABLE `Inquilinos`
  MODIFY `ID_INQUILINO` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `Pago`
--
ALTER TABLE `Pago`
  MODIFY `ID_PAGO` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `Solicitudes`
--
ALTER TABLE `Solicitudes`
  MODIFY `ID_SOLICITUD` int NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Cuartos`
--
ALTER TABLE `Cuartos`
  ADD CONSTRAINT `Cuartos_ibfk_1` FOREIGN KEY (`ID_INQUILINO`) REFERENCES `Inquilinos` (`ID_INQUILINO`);

--
-- Filtros para la tabla `Cuotas`
--
ALTER TABLE `Cuotas`
  ADD CONSTRAINT `Cuotas_ibfk_1` FOREIGN KEY (`ID_CUARTO`) REFERENCES `Cuartos` (`ID_CUARTO`);

--
-- Filtros para la tabla `Pago`
--
ALTER TABLE `Pago`
  ADD CONSTRAINT `Pago_ibfk_1` FOREIGN KEY (`ID_CUOTA`) REFERENCES `Cuotas` (`ID_CUOTA`);

--
-- Filtros para la tabla `Solicitudes`
--
ALTER TABLE `Solicitudes`
  ADD CONSTRAINT `Solicitudes_ibfk_1` FOREIGN KEY (`ID_INQUILINO`) REFERENCES `Inquilinos` (`ID_INQUILINO`),
  ADD CONSTRAINT `Solicitudes_ibfk_2` FOREIGN KEY (`ID_ADMINISTRADOR`) REFERENCES `Administradores` (`ID_ADMINISTRADOR`);

DELIMITER $$
--
-- Eventos
--
CREATE DEFINER=`admin`@`localhost` EVENT `ActualizarDatosInquilinosEvent` ON SCHEDULE EVERY 1 DAY STARTS '2024-04-01 19:05:00' ON COMPLETION NOT PRESERVE ENABLE DO CALL ActualizarDatosInquilinos()$$

CREATE DEFINER=`admin`@`localhost` EVENT `ActualizarEstadoCuartosEvent` ON SCHEDULE EVERY 1 DAY STARTS '2024-04-01 19:10:00' ON COMPLETION NOT PRESERVE ENABLE DO CALL ActualizarEstadoCuartos()$$

CREATE DEFINER=`admin`@`localhost` EVENT `GenerarCuotasMensualesEvent` ON SCHEDULE EVERY 1 MONTH STARTS '2024-04-01 19:00:00' ON COMPLETION NOT PRESERVE ENABLE DO CALL GenerarCuotasMensuales()$$

CREATE DEFINER=`admin`@`localhost` EVENT `actualizar_estados_cuotas_event` ON SCHEDULE EVERY 1 DAY STARTS '2024-04-20 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO CALL actualizar_estados_cuotas_proc()$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
