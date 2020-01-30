UPDATE `thattemplate`
SET `EMT_CONTENT` = '<html>
						<head>
							<meta charset=\"utf-8\"/>
						</head>
						<body style=\"color:#ffffff;font-size:16px;\">
						<div style=\"background-color:#111b24;border-radius: 2em;\">
							<div style=\"background-color:#394656;border-radius:2em;text-align:left;background-image: url(\'https://s3-us-west-2.amazonaws.com/aporlaofertaimages/static/background_head5.png\');\">
								<div style=\"text-align:center;\">
									<img style=\"width:100px;height:100px;\"
										 src=\"https://s3-us-west-2.amazonaws.com/aporlaofertaimages/static/logo2.png\"/>
								</div>
								<div style=\"margin-top:1em;text-align:center;\">
									<img src=\"${avatarsrc}\"
										 style=\"width: 200px;height: 200px;text-align:left;border: solid 3px #ADADAD;border-radius: 20px;\"/>
									<br/>Hola ${nickname}!<br/>
								</div>
								<div style=\"text-align:center;margin-top:1em;color:#ffffff;\">
									Para confirmar tu usuario sigue el siguiente enlace:<br/><br/>

									<h3>
										<a style=\"margin-top:1em;background-color: #618eb8;padding:.8em;color:#ffffff;border-radius:.5em;border:solid 2px #618eb8;\"
										   href=\"${server}/confirmUser?user=${nickname}&confirmationID=${userid}\" target=\"_blank\">
											Confirmar usuario
										</a>
									</h3>
									<br/>

									<div style=\"text-align:center;margin-left:1em;color:#ffffff;\">
										O copia y pega la siguiente dirección url en tu navegador:<br/>
										<h4 style=\"text-align:center;color:#618eb8;\">
											<a href=\"${server}/confirmUser?user=${nickname}&confirmationID=${userid}\" style=\"color:#618eb8;\">
												${server}/confirmUser?user=${nickname}&confirmationID=${userid}
											</a>
										</h4>
									</div>
								</div>
								<div style=\"text-align:center;color:#ffffff;\">Nos vemos en
									<a href=\"${server}\" target=\"_blank\" style=\"color:#618eb8;\"> aporlaoferta.com!</a>
								</div>
								<br/>

								<div style=\"text-align:center;font-size:12px;color:#ffffff;\">aporlaoferta 2015</div>
							</div>
						</div>
						</body>
						</html>',
  `EMT_SUBJECT` = 'Bienvenido a aporlaoferta.com!'
WHERE `EMT_ID`=3