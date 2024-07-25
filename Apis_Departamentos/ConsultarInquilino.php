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

// Obtener el ID del inquilino (reemplaza con la forma en que pasas el ID)
$id_inquilino = $_GET['id_inquilino'];

// Llamar al procedimiento almacenado
$sql = "CALL ObtenerDatosInquilinoYCuarto($id_inquilino)";

$resultado = $conexion->query($sql);

$response = array(); // Array para almacenar la respuesta

if ($resultado->num_rows > 0) {
    // Convertir el resultado en un array asociativo
    $fila = $resultado->fetch_assoc();
    
    // Construir el array de respuesta
    $response['inquilino'] = array(
        'Nombre' => $fila["Nombre_Inquilino"],
        'DNI' => $fila["DNI_Inquilino"],
        'Telefono' => $fila["Telefono_Inquilino"],
        'FechaIngreso' => $fila["Fecha_Ingreso_Inquilino"],
        'Pagos' => $fila["Pagos_Inquilino"],
        'Deudas' => $fila["Deudas_Inquilino"],
        'Solicitudes' => $fila["Solicitudes_Inquilino"]
    );

    $response['cuarto'] = array(
        'ID' => $fila["ID_Cuarto"],
        'Codigo' => $fila["Codigo_Cuarto"],
        'Tamano' => $fila["Tamano_Cuarto"],
        'Piso' => $fila["Piso_Cuarto"],
        'Costo' => $fila["Costo_Cuarto"],
        'Estado' => $fila["Estado_Cuarto"]
    );
} else {
    $response['message'] = "No se encontraron resultados para el ID de inquilino especificado.";
}

// Cerrar la conexión
$conexion->close();

// Convertir el array de respuesta a JSON y mostrarlo
echo json_encode($response);
?>
