<?php
$hostname = 'localhost';
$database = '';
$username = '';
$password = '';

// Verificar si todos los parámetros necesarios están presentes y no son nulos
if(isset($_POST["id_inquilino"], $_POST["id_administrador"], $_POST["nombre_sol"], $_POST["descripcion_sol"], $_POST["fecha_sol"], $_POST["estado_sol"], $_POST["monto_sol"])) {
    // Obtener los parámetros enviados por POST
    $id_inquilino = $_POST["id_inquilino"];
    $id_administrador = $_POST["id_administrador"];
    $nombre_sol = $_POST["nombre_sol"];
    $descripcion_sol = $_POST["descripcion_sol"];
    $fecha_sol = $_POST["fecha_sol"];
    $estado_sol = $_POST["estado_sol"];
    $monto_sol = $_POST["monto_sol"];

    // Crear la conexión
    $conexion = new mysqli($hostname, $username, $password, $database);

    // Verificar la conexión
    if ($conexion->connect_error) {
        die("Error de conexión: " . $conexion->connect_error);
    }

    // Llamar al procedimiento almacenado
    $sql = "CALL InsertarSolicitud(?, ?, ?, ?, ?, ?, ?)";
    $stmt = $conexion->prepare($sql);

    // Especificar el tipo de datos de cada parámetro
    $stmt->bind_param("iissssi", $id_inquilino, $id_administrador, $nombre_sol, $descripcion_sol, $fecha_sol, $estado_sol, $monto_sol);

    $stmt->execute();

    // Cerrar conexión
    $stmt->close();
    $conexion->close();
} else {
    // Si falta algún parámetro, mostrar un mensaje de error
    echo "Error: Todos los parámetros son necesarios y no pueden ser nulos";
}
?>
