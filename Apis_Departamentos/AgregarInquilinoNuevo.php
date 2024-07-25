<?php
$hostname = 'localhost';
$database = '';
$username = '';
$password = '';

// Verificar si todos los parámetros necesarios están presentes y no son nulos
if(isset($_POST["dni"], $_POST["nombre"], $_POST["contrasena"], $_POST["nomusuario"], $_POST["telefono"], $_POST["fechaingreso"], $_POST["pagos"], $_POST["deudas"], $_POST["solicitudes"])) {
    // Obtener los parámetros enviados por POST
    $dni = $_POST["dni"];
    $nombre = $_POST["nombre"];
    $contrasena = $_POST["contrasena"];
    $nomusuario = $_POST["nomusuario"];
    $telefono = $_POST["telefono"];
    $fechaingreso = $_POST["fechaingreso"];
    $pagos = $_POST["pagos"];
    $deudas = $_POST["deudas"];
    $solicitudes = $_POST["solicitudes"];

    // Crear la conexión
    $conexion = new mysqli($hostname, $username, $password, $database);

    // Verificar la conexión
    if ($conexion->connect_error) {
        die("Error de conexión: " . $conexion->connect_error);
    }

    // Llamar al procedimiento almacenado
    $sql = "CALL InsertarNuevoInquilino(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    $stmt = $conexion->prepare($sql);

    // Especificar el cotejamiento de caracteres para cada parámetro
    $stmt->bind_param("ssssssiii", $dni, $nombre, $contrasena, $nomusuario, $telefono, $fechaingreso, $pagos, $deudas, $solicitudes);

    $stmt->execute();

    // Obtener el resultado del procedimiento almacenado
    $stmt->bind_result($id_inquilino, $resultado);
    $stmt->fetch();

    // Mostrar la respuesta
    if($resultado == "Ya existe un usuario con el mismo DNI"){
        echo $resultado;
    } else {
        echo $id_inquilino;
    }

    // Cerrar conexión
    $stmt->close();
    $conexion->close();
} else {
    // Si falta algún parámetro, mostrar un mensaje de error
    echo "Error: Todos los parámetros son necesarios y no pueden ser nulos";
}
?>
