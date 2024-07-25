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
if (!isset($_POST['pago_id'], $_POST['nuevo_estado'], $_POST['razon_pago'])) {
    die("Error: Se requieren todos los parámetros 'pago_id', 'nuevo_estado' y 'razon_pago'.");
}

$pago_id = $_POST['pago_id'];
$nuevo_estado = $_POST['nuevo_estado'];
$razon_pago = $_POST['razon_pago'];

// Llama al procedimiento almacenado para actualizar el estado del pago
$actualizarProcedimiento = "CALL actualizar_estado_pago(?, ?, ?)";
$stmt = $conexion->prepare($actualizarProcedimiento);
$stmt->bind_param('iss', $pago_id, $nuevo_estado, $razon_pago);

if ($stmt->execute()) {
    echo "Datos actualizados correctamente.";
} else {
    echo "Error al actualizar los datos: " . $stmt->error;
}

$stmt->close();
$conexion->close();
?>
