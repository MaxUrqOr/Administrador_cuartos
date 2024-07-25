<?php
$hostname = 'localhost';
$database = '';
$username = '';
$password = '';

// Verificar si todos los parámetros necesarios están presentes y no son nulos
if(isset($_POST["id_cuarto"], $_POST["nuevo_costo"], $_POST["nueva_garantia"])) {
    // Obtener los parámetros enviados por POST
    $id_cuarto = $_POST["id_cuarto"];
    $nuevo_costo = $_POST["nuevo_costo"];
    $nueva_garantia = $_POST["nueva_garantia"];

    // Crear la conexión
    $conexion = new mysqli($hostname, $username, $password, $database);

    // Verificar la conexión
    if ($conexion->connect_error) {
        die("Error de conexión: " . $conexion->connect_error);
    }

    // Llamar al procedimiento almacenado
    $sql = "CALL Actualizar_costo_garantia_cuarto(?, ?, ?)";
    $stmt = $conexion->prepare($sql);

    // Verificar si la preparación de la declaración fue exitosa
    if ($stmt === false) {
        die("Error en la preparación de la declaración: " . $conexion->error);
    }

    // Especificar el tipo de datos de cada parámetro
    $stmt->bind_param("iii", $id_cuarto, $nuevo_costo, $nueva_garantia);

    if ($stmt->execute()) {
        echo "Costo y garantía del cuarto actualizados correctamente.";
    } else {
        echo "Error al actualizar el costo y la garantía del cuarto: " . $stmt->error;
    }

    // Cerrar conexión
    $stmt->close();
    $conexion->close();
} else {
    // Si falta algún parámetro, mostrar un mensaje de error
    echo "Error: Todos los parámetros son necesarios y no pueden ser nulos";
}
?>

