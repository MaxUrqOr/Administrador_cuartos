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

// Verificar si se recibieron todos los parámetros necesarios
if (!isset($_POST['id_cuota_param'], $_POST['fecha_pag_param'], $_POST['monto_pag_param'], $_POST['estado_pag_param'], $_POST['razon_pag_param'])) {
    die("Error: Se requieren todos los parámetros");
}

$id_cuota_param = $_POST['id_cuota_param'];
$fecha_pag_param = $_POST['fecha_pag_param'];
$monto_pag_param = $_POST['monto_pag_param'];
$estado_pag_param = $_POST['estado_pag_param'];
$razon_pag_param = $_POST['razon_pag_param'];

// Llama al procedimiento almacenado para actualizar los datos del inquilino
$insertar = "CALL InsertarPago(?, ?, ?, ?, ?)";
$stmt = $conexion->prepare($insertar);
$stmt->bind_param('isiss', $id_cuota_param, $fecha_pag_param, $monto_pag_param, $estado_pag_param, $razon_pag_param);

if ($stmt->execute()) {
    echo "Datos del inquilino actualizados correctamente.";
} else {
    echo "Error al actualizar los datos del inquilino: " . $stmt->error;
}

$stmt->close();
$conexion->close();
?>
