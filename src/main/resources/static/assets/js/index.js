//crea elemento
const video = document.createElement("video");

//nuestro camvas
const canvasElement = document.getElementById("qr-canvas");
const canvas = canvasElement.getContext("2d");

//div donde llegara nuestro canvas
const btnScanQR = document.getElementById("btn-scan-qr");

//lectura desactivada
let scanning = false;

//funcion para encender la camara
const encenderCamara = () => {
  navigator.mediaDevices
    .getUserMedia({ video: { facingMode: "environment" } })
    .then(function (stream) {
      scanning = true;
      btnScanQR.hidden = true;
      canvasElement.hidden = false;
      video.setAttribute("playsinline", true); // required to tell iOS safari we don't want fullscreen
      video.srcObject = stream;
      video.play();
      tick();
      scan();
    })
    .catch(function (err) {
      console.error("Error al acceder a la cámara: ", err);
      alert("Error al acceder a la cámara: " + err.message);
    });
};

//funciones para levantar las funiones de encendido de la camara
function tick() {
  canvasElement.height = video.videoHeight;
  canvasElement.width = video.videoWidth;
  canvas.drawImage(video, 0, 0, canvasElement.width, canvasElement.height);

  scanning && requestAnimationFrame(tick);
}

function scan() {
  try {
    qrcode.decode();
  } catch (e) {
    setTimeout(scan, 300);
  }
}

//apagara la camara
const cerrarCamara = () => {
  video.srcObject.getTracks().forEach((track) => {
    track.stop();
  });
  canvasElement.hidden = true;
  btnScanQR.hidden = false;
};

const activarSonido = () => {
  var audio = document.getElementById('audioScaner');
  audio.play();
}

//callback cuando termina de leer el codigo QR
qrcode.callback = (respuestaEncriptada) => {
  if (respuestaEncriptada) {
    Swal.fire({
      title: 'Consultando QR...',
      didOpen: () => {
        Swal.showLoading();
      }
    });

    fetch('/entradas/validar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ identificador: respuestaEncriptada }),
    })
    .then(response => response.json())
    .then(data => {
      Swal.close(); // Cerrar el spinner de carga

      // Revisar el mensaje que viene del backend y actuar en consecuencia
      if (data.mensaje === 'aprobado') {
        Swal.fire('Este QR ya ha sido aprobado');
      } else if (data.mensaje === 'no_aprobado') {
        Swal.fire({
          title: 'Este QR no está aprobado',
          text: '¿Deseas aprobarlo?',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Aprobar',
          cancelButtonText: 'Cancelar'
        }).then((result) => {
          if (result.isConfirmed) {
            // Llamar al backend para aprobar el QR
            aprobarQr(respuestaEncriptada);
          }
        });
      } else if (data.mensaje === 'no_existe') {
        Swal.fire('El QR no existe, posible falsificación.');
      }
    })
    .catch(error => {
      console.error('Error validando QR:', error);
      Swal.fire('Error validando QR', error.message, 'error');
    });

    // Swal.fire(respuestaEncriptada)
    // activarSonido();
    // cerrarCamara();    

  }
};

// Función para validar el código introducido manualmente
// Función para consultar el código introducido manualmente
const consultarCodigo = () => {
  const codigoInput = document.getElementById("codigoInput").value.trim(); // Asegúrate de que no haya espacios vacíos

  if (!codigoInput) {
    Swal.fire('Por favor, introduzca un código.');
    return;
  }

  Swal.fire({
    title: 'Consultando código...',
    didOpen: () => {
      Swal.showLoading();
    }
  });

  fetch('/entradas/validar-codigo', {  // Asegúrate que la ruta aquí sea correcta
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ codigo: codigoInput }),
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`Error en la consulta: ${response.status}`);
    }
    return response.json();
  })
  .then(data => {
    Swal.close(); // Cerrar el spinner de carga

    // Validación del mensaje del backend
    if (data.mensaje === 'aprobado') {
      Swal.fire('Este código ya ha sido aprobado');
    } else if (data.mensaje === 'no_aprobado') {
      Swal.fire({
        title: 'Este código no está aprobado',
        text: '¿Deseas aprobarlo?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Aprobar',
        cancelButtonText: 'Cancelar'
      }).then((result) => {
        if (result.isConfirmed) {
          aprobarCodigo(codigoInput); // Llamar a la función de aprobación si el usuario lo confirma
        }
      });
    } else if (data.mensaje === 'no_existe') {
      Swal.fire('El código no existe, posible falsificación.');
    } else {
      Swal.fire('Respuesta inesperada del servidor');
    }

    // Limpiar el campo de entrada después de la consulta
    document.getElementById("codigoInput").value = '';

  })
  .catch(error => {
    console.error('Error validando código:', error);
    Swal.fire('Error validando código', error.message, 'error');
  });
};

// Función para aprobar el código manualmente
const aprobarCodigo = (codigo) => {
  fetch('/entradas/aprobar-codigo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ codigo: codigo }),
  })
  .then(response => response.json())
  .then(data => {
    Swal.fire(data.mensaje);
  })
  .catch(error => {
    console.error('Error aprobando código:', error);
    Swal.fire('Error aprobando código', error.message, 'error');
  });
};


const aprobarQr = (identificador) => {
  fetch('/entradas/aprobar', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ identificador: identificador }),
  })
  .then(response => response.json())
  .then(data => {
    Swal.fire(data.mensaje); // Mostrar mensaje de aprobación o de error
  })
  .catch(error => {
    console.error('Error aprobando QR:', error);
    Swal.fire('Error aprobando QR', error.message, 'error');
  });
};

window.addEventListener('load', (e) => {
  encenderCamara();
})