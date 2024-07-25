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

// Llamar al procedimiento almacenado
$sql = "CALL ConsultarTodosCuartos()";

$result = $conexion->query($sql);

if ($result->num_rows > 0) {
    // Inicializar array para almacenar los resultados
    $cuartos = array();

    // Agregar cada fila de resultados al array
    while($row = $result->fetch_assoc()) {
        $cuartos[] = $row;
    }

    // Crear un array asociativo para contener el array de cuartos
    $response = array(
        'cuartos' => $cuartos
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
