<?php
$hostname = 'localhost';
$database = '';
$username = '';
$password = '';

// Crear la conexión
$conexion = new mysqli($hostname, $username, $password, $database);

// Verificar la conexión
if ($conexion->connect_error) {
    die("Error de conexión: " . $conexion->connect_error);
}

// Obtener el ID del administrador de la solicitud (reemplaza con la forma en que pasa el ID)
$admin_id = $_GET['admin_id'];

// Llamar al procedimiento almacenado
$sql = "CALL ConsultarAdministradorPorID($admin_id)";

$resultado = $conexion->query($sql);

$response = array(); // Array para almacenar la respuesta

if ($resultado->num_rows > 0) {
    // Convertir el resultado en un array asociativo
    $fila = $resultado->fetch_assoc();
    
    // Construir el array de respuesta
    $response['administrador'] = array(
        'ID' => $fila["ID_ADMINISTRADOR"],
        'DNI' => $fila["DNI_ADM"],
        'Nombre' => $fila["NOMBRE_ADM"],
        'Apellido' => $fila["APELLIDOS_ADM"],
        'Contrasena' => $fila["CONTRASENA_ADM"],
        'Usuario' => $fila["NOMUSUARIO_ADM"],
        'Correo' => $fila["CORREO_ADM"],
	'Tipo_administrador' => $fila["TIPO_ADM"],
        'cantidad_pagos' => $fila["cantidad_pagos"],
        'cantidad_solicitudes' => $fila["cantidad_solicitudes"],
        'cantidad_cuartos_libres' => $fila["cantidad_cuartos_libres"]
        // Añadir otras columnas del administrador según sea necesario
    );
} else {
    $response['message'] = "No se encontraron resultados para el ID de administrador especificado.";
}

// Cerrar la conexión
$conexion->close();

// Convertir el array de respuesta a JSON y mostrarlo
echo json_encode($response);
?>
