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

$p_usuario = $_POST['p_usuario'];
$p_contrasena = $_POST['p_contrasena'];

// Verificar si los datos están llegando correctamente
echo "Usuario recibido: " . $_POST['p_usuario'] . "<br>";
echo "Contraseña recibida: " . $_POST['p_contrasena'] . "<br>";

// Llamada al procedimiento almacenado
if ($stmt = $conexion->prepare("CALL ValidarAdminUsuarioContrasena(?, ?, @p_valido, @p_ID_Usuario)")) {
	$stmt->bind_param("ss", $p_usuario, $p_contrasena);

    if ($stmt->execute()) {
        // La ejecución fue exitosa, ahora obtendremos el resultado
        $selectResult = $conexion->query('SELECT @p_valido, @p_ID_Usuario');
        $resultado = $selectResult->fetch_assoc();

        // Crear un arreglo para enviar la respuesta en formato JSON
        $respuesta = array(
            'p_valido' => (int)$resultado['@p_valido'],
            'p_ID_Usuario' => (int)$resultado['@p_ID_Usuario']
        );

        // Enviar la respuesta en formato JSON
        echo json_encode($respuesta);
    } else {
        // Error al ejecutar el procedimiento almacenado
        $respuesta = array('p_valido' => 0);
        echo json_encode($respuesta);
    }
	$stmt->close();
} else {
    // Error al preparar la llamada al procedimiento almacenado
    $respuesta = array('p_valido' => 0);
    echo json_encode($respuesta);
}

$conexion->close();
?>