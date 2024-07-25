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

// Verificar si se recibió el parámetro 'pago_id'
if (!isset($_GET['pago_id'])) {
    die("Error: Se requiere el parámetro 'pago_id'");
}

$pago_id = $_GET['pago_id'];

// Llamar al procedimiento almacenado para obtener la foto del pago
$sql = "CALL ObtenerFotoPago(?)";
$stmt = $conexion->prepare($sql);
$stmt->bind_param('i', $pago_id);

if ($stmt->execute()) {
    // Vincular los resultados de la consulta
    $stmt->bind_result($foto_pago, $url_foto);
    
    // Recuperar los resultados
    $stmt->fetch();
    
    // Crear un array asociativo con la URL de la foto
    $response = array(
	'foto_base64'=> 'Nombre del pago',
        'url_foto' => $url_foto
    );
    
    // Convertir el array a formato JSON y mostrarlo
    echo json_encode($response);
} else {
    echo "Error al obtener la foto del pago: " . $conexion->error;
}

// Cerrar la conexión
$stmt->close();
$conexion->close();
?>

