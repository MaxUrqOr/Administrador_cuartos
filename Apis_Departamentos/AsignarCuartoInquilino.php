<?php
$hostname = 'localhost';
$database = '';
$username = '';
$password = '';

// Verificar si todos los parámetros necesarios están presentes y no son nulos
if(isset($_POST["id_inquilino"], $_POST["id_cuarto"])) {
    // Obtener los parámetros enviados por POST
    $id_inquilino = $_POST["id_inquilino"];
    $id_cuarto = $_POST["id_cuarto"];

    // Crear la conexión
    $conexion = new mysqli($hostname, $username, $password, $database);

    // Verificar la conexión
    if ($conexion->connect_error) {
        die("Error de conexión: " . $conexion->connect_error);
    }

    // Llamar al procedimiento almacenado
    $sql = "CALL AsignarInquilinoACuarto(?, ?)";
    $stmt = $conexion->prepare($sql);

    // Especificar el tipo de datos para cada parámetro
    $stmt->bind_param("ii", $id_inquilino, $id_cuarto);

    $stmt->execute();

    // Cerrar conexión
    $stmt->close();
    $conexion->close();

    echo "Procedimiento ejecutado exitosamente";
} else {
    // Si falta algún parámetro, mostrar un mensaje de error
    echo "Error: Todos los parámetros son necesarios y no pueden ser nulos";
}
?>
