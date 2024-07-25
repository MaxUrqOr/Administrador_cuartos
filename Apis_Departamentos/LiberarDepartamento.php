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
if (!isset($_POST['inquilino_id'])) {
    die("Error: Se requiere el parámetro 'inquilino_id'");
}

$inquilino_id = $_POST['inquilino_id'];

// Llama al procedimiento almacenado para liberar el cuarto
$actualizarProcedimiento = "CALL LiberarCuarto(?)";
$stmt = $conexion->prepare($actualizarProcedimiento);
$stmt->bind_param('i', $inquilino_id);

if ($stmt->execute()) {
    echo "Cuarto liberado correctamente.";
} else {
    echo "Error al liberar el cuarto: " . $stmt->error;
}

$stmt->close();
$conexion->close();
?>
