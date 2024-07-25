<?php
$hostname = 'localhost';
$database = '';
$username = '';
$password = '';

$response = array();

try {
    // Crear la conexión
    $conexion = new mysqli($hostname, $username, $password, $database);

    // Verificar la conexión
    if ($conexion->connect_error) {
        throw new Exception("Error de conexión: " . $conexion->connect_error);
    }

    // Obtener los datos del formulario y verificar si están presentes
    if (isset($_POST['ID_CUOTA'], $_POST['FECHA_PAG'], $_POST['FOTO_PAG'], $_POST['ESTADO_PAG'], $_POST['RAZON_PAGO'])) {  
        $p_ID_CUOTA = $_POST['ID_CUOTA'];
        $p_FECHA_PAG = $_POST['FECHA_PAG'];
        $p_FOTO_PAG = $_POST['FOTO_PAG']; // Imagen en base64
        $p_ESTADO_PAG = $_POST['ESTADO_PAG']; // Estado del pago
        $p_RAZON_PAGO = $_POST['RAZON_PAGO']; // Razón del pago

        // Log para verificar los parámetros recibidos
        error_log("ID_CUOTA: $p_ID_CUOTA, FECHA_PAG: $p_FECHA_PAG, ESTADO_PAG: $p_ESTADO_PAG, RAZON_PAGO: $p_RAZON_PAGO");

        // Decodificar la imagen base64 y guardarla en un archivo
        $decoded_image = base64_decode($p_FOTO_PAG);
        if ($decoded_image === false) {
            throw new Exception("Error al decodificar la imagen base64.");
        }

        $nombre_imagen = uniqid() . '.jpg';
        $ruta_imagen = '/var/www/html/Apis_Departamentos/Imagenes/' . $nombre_imagen;
        if (!file_put_contents($ruta_imagen, $decoded_image)) {
            throw new Exception("Error al guardar la imagen en el servidor.");
        }

        // Llamar al procedimiento almacenado
        $query = "CALL ActualizarPagoFoto(?, ?, ?, ?, ?)";
        $stmt = $conexion->prepare($query);
        if (!$stmt) {
            throw new Exception("Error al preparar la llamada al procedimiento almacenado: " . $conexion->error);
        }

        // Vincular parámetros
        $stmt->bind_param("issss", $p_ID_CUOTA, $p_FECHA_PAG, $nombre_imagen, $p_ESTADO_PAG, $p_RAZON_PAGO);

        if ($stmt->execute()) {
            // Éxito al insertar el pago
            $response['success'] = true;
            $response['message'] = "Pago insertado correctamente.";
        } else {
            // Error al ejecutar el procedimiento almacenado
            $response['success'] = false;
            $response['message'] = "Error al insertar el pago: " . $stmt->error;
        }
        $stmt->close();
    } else {
        // Log para verificar los parámetros recibidos
        error_log("POST data: " . print_r($_POST, true));

        // Datos del formulario incompletos o no recibidos
        $response['success'] = false;
        $response['message'] = "Datos del formulario incompletos o no recibidos.";
    }
} catch (Exception $e) {
    // Capturar excepciones y proporcionar un mensaje de error
    $response['success'] = false;
    $response['message'] = $e->getMessage();
}

// Cerrar la conexión
if (isset($conexion)) {
    $conexion->close();
}

// Enviar la respuesta en formato JSON
echo json_encode($response);
?>

