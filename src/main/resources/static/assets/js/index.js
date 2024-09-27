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
      video.setAttribute("playsinline", true);
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
  var audio = document.getElementById("audioScaner");
  audio.play();
};

qrcode.callback = (respuestaEncriptada) => {
  if (respuestaEncriptada) {
    Swal.fire({
      title: "Consultando QR...",
      didOpen: () => {
        Swal.showLoading();
      },
    });

    fetch("/entradas/validar", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ identificador: respuestaEncriptada }),
    })
      .then((response) => response.json())
      .then((data) => {
        Swal.close();

        if (data.mensaje === "aprobado") {
          Swal.fire("Este QR ya ha sido aprobado");
        } else if (data.mensaje === "no_aprobado") {
          Swal.fire({
            title: "Este QR no está aprobado",
            text: "¿Deseas aprobarlo?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Aprobar",
            cancelButtonText: "Cancelar",
          }).then((result) => {
            if (result.isConfirmed) {
              aprobarQr(respuestaEncriptada);
            }
          });
        } else if (data.mensaje === "no_existe") {
          Swal.fire("El QR no existe, posible falsificación.");
        }
      })
      .catch((error) => {
        console.error("Error validando QR:", error);
        Swal.fire("Error validando QR", error.message, "error");
      });

    // Swal.fire(respuestaEncriptada)
    // activarSonido();
    // cerrarCamara();
  }
};

const consultarCodigo = () => {
    console.log("Función consultarCodigo ejecutada");
    const codigoInput = $('#codigoInput').val().trim();
    if (!codigoInput) {
        Swal.fire("Por favor, introduzca un código.");
        return;
    }

    Swal.fire({
        title: "Consultando código...",
        didOpen: () => {
            Swal.showLoading();
        },
    });

    $.ajax({
        url: "/entradas/validar-codigo",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ codigo: codigoInput }),
        success: function (data) {
            Swal.close();

            if (data.mensaje === "aprobado") {
                Swal.fire("Este código ya ha sido aprobado");
            } else if (data.mensaje === "no_aprobado") {
                Swal.fire({
                    title: "Este código no está aprobado",
                    text: "¿Deseas aprobarlo?",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonText: "Aprobar",
                    cancelButtonText: "Cancelar",
                }).then((result) => {
                    if (result.isConfirmed) {
                        aprobarCodigo(codigoInput);
                    }
                });
            } else if (data.mensaje === "no_existe") {
                Swal.fire("El código no existe, posible falsificación.");
            } else {
                Swal.fire("Respuesta inesperada del servidor");
            }

            $('#codigoInput').val('');
        },
        error: function (xhr, status, error) {
            console.error("Error validando código:", error);
            Swal.fire("Error validando código", error, "error");
        }
    });
};

const aprobarCodigo = (codigo) => {
    $.ajax({
        url: "/entradas/aprobar-codigo",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ codigo: codigo }),
        success: function (data) {
            Swal.fire(data.mensaje);
        },
        error: function (xhr, status, error) {
            console.error("Error aprobando código:", error);
            Swal.fire("Error aprobando código", error, "error");
        }
    });
};



const aprobarQr = (identificador) => {
  fetch("/entradas/aprobar", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ identificador: identificador }),
  })
    .then((response) => response.json())
    .then((data) => {
      Swal.fire(data.mensaje);
    })
    .catch((error) => {
      console.error("Error aprobando QR:", error);
      Swal.fire("Error aprobando QR", error.message, "error");
    });
};

window.addEventListener("load", (e) => {
  encenderCamara();
});
