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

// Obtener el valor del parámetro 'codigo_cuarto' desde la URL (a través de GET)
$codigo_cuarto = $_GET['codigo_cuarto'];

// Llamar al procedimiento almacenado
$sql = "CALL ConsultarDatosCuotaConPagosPorCuartoPagados('$codigo_cuarto')";

$result = $conexion->query($sql);

if ($result->num_rows > 0) {
    // Inicializar array para almacenar los resultados
    $pagos = array();

    // Agregar cada fila de resultados al array
    while($row = $result->fetch_assoc()) {
        $pagos[] = $row;
    }

    // Crear un array asociativo para contener el array de pagos
    $response = array(
        'pagos' => $pagos
    );

    // Convertir el array a formato JSON
    $json_response = json_encode($response);

    // Imprimir el JSON
    echo $json_response;
} else {
    echo json_encode(array('message' => 'No se encontraron resultados para el código de cuarto especificado.'));
}

// Cerrar la conexión
$conexion->close();
?>
