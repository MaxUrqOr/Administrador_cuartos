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
if (!isset($_POST['p_id_inquilino'], $_POST['p_dni'], $_POST['p_nombre'], $_POST['p_telefono'], $_POST['p_fechaingreso'])) {
    die("Error: Se requieren todos los parámetros 'p_id_inquilino', 'p_dni', 'p_nombre', 'p_telefono' y 'p_fechaingreso'.");
}

$p_id_inquilino = $_POST['p_id_inquilino'];
$p_dni = $_POST['p_dni'];
$p_nombre = $_POST['p_nombre'];
$p_telefono = $_POST['p_telefono'];
$p_fechaingreso = $_POST['p_fechaingreso'];

// Llama al procedimiento almacenado para actualizar los datos del inquilino
$actualizarProcedimiento = "CALL EditarInquilino(?, ?, ?, ?, ?)";
$stmt = $conexion->prepare($actualizarProcedimiento);
$stmt->bind_param('issss', $p_id_inquilino, $p_dni, $p_nombre, $p_telefono, $p_fechaingreso);

if ($stmt->execute()) {
    echo "Datos del inquilino actualizados correctamente.";
} else {
    echo "Error al actualizar los datos del inquilino: " . $stmt->error;
}

$stmt->close();
$conexion->close();
?>
