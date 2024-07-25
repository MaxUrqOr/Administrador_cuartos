<?php
$hostname = 'localhost';
$database = '';
$username = '';
$password = '';

// Directorio donde se guardarán las imágenes
$directorio_imagenes = '/var/www/html/Apis_Departamentos/Imagenes/';

// Crear la conexión
$conexion = new mysqli($hostname, $username, $password, $database);

// Verificar la conexión
if ($conexion->connect_error) {
    die("Error de conexión: " . $conexion->connect_error);
}

// Obtener los datos del formulario y verificar si están presentes
if (isset($_POST['ID_CUOTA'], $_POST['FECHA_PAG'], $_POST['MONTO_PAG'], $_POST['FOTO_PAG'], $_POST['ESTADO_PAG'], $_POST['RAZON_PAGO'])) {
    $p_ID_CUOTA = $_POST['ID_CUOTA'];
    $p_FECHA_PAG = $_POST['FECHA_PAG'];
    $p_MONTO_PAG = $_POST['MONTO_PAG'];
    $p_FOTO_PAG = $_POST['FOTO_PAG']; // Imagen en base64
    $p_ESTADO_PAG = $_POST['ESTADO_PAG']; // Estado del pago
    $p_RAZON_PAGO = $_POST['RAZON_PAGO']; // Razón del pago

    // Decodificar la imagen base64 y guardarla en un archivo
    $decoded_image = base64_decode($p_FOTO_PAG);
    $nombre_imagen = uniqid() . '.jpg';
    $ruta_imagen = $directorio_imagenes . $nombre_imagen;
    file_put_contents($ruta_imagen, $decoded_image);

    // Llamar al procedimiento almacenado
    if ($stmt = $conexion->prepare("CALL InsertarPagocFoto(?, ?, ?, ?, ?, ?, ?)")) {
        // Vincular parámetros
        $stmt->bind_param("isibsss", $p_ID_CUOTA, $p_FECHA_PAG, $p_MONTO_PAG, $ruta_imagen, $nombre_imagen, $p_ESTADO_PAG, $p_RAZON_PAGO);

        if ($stmt->execute()) {
            // Éxito al insertar el pago
            $response['success'] = true;
            $response['message'] = "Pago insertado correctamente.";
        } else {
            // Error al ejecutar el procedimiento almacenado
            $response['success'] = false;
            $response['message'] = "Error al insertar el pago.";
        }
        $stmt->close();
    } else {
        // Error al preparar la llamada al procedimiento almacenado
        $response['success'] = false;
        $response['message'] = "Error al preparar la llamada al procedimiento almacenado.";
    }
} else {
    // Datos del formulario incompletos o no recibidos
    $response['success'] = false;
    $response['message'] = "Datos del formulario incompletos o no recibidos.";
}

// Cerrar la conexión
$conexion->close();

// Enviar la respuesta en formato JSON
echo json_encode($response);
?>
