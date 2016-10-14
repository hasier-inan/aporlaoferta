UPDATE `thattemplate`
SET `EMT_CONTENT` = '<html>
						<head>
							<meta charset=\"utf-8\"/>
						</head>
						<body style=\"color:#ffffff;font-size:16px;\">
						<div style=\"background-color:#111b24;border-radius: 2em;\">
							<div style=\"background-color:#394656;border-radius:2em;text-align:left;margin:4em 4em 4em 4em;background-image: url(\'https://s3-us-west-2.amazonaws.com/aporlaofertaimages/static/background_head5.png\');\">
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
									Si NO has realizado una petición para reiniciar la contraseña en <a href=\"aporlaoferta.com\"
																										style=\"color:#ffffff;\">aporlaoferta.com</a>
									puedes ignorar este mensaje.<br/>
									Para reiniciar tu contraseña de usuario sigue el siguiente enlace:<br/><br/>

									<h2>
										<a style=\"margin-top:1em;background-color: #618eb8;padding:.8em;color:#ffffff;border-radius:.5em;border:solid 2px #618eb8;\"
										   href=\"${server}/passwordForgotten?user=${nickname}&track=${userid}\" target=\"_blank\">
											Reiniciar contraseña
										</a>
									</h2>
									<br/>

									<div style=\"text-align:center;margin-left:1em;color:#ffffff;\">
										O copia y pega la siguiente dirección url en tu navegador:<br/>
										<h4 style=\"text-align:center;color:#618eb8;\">
											<a href=\"${server}/passwordForgotten?user=${nickname}&track=${userid}\" style=\"color:#618eb8;\">
												${server}/passwordForgotten?user=${nickname}&track=${userid}
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
  `EMT_SUBJECT` = 'Petición de cambio de contraseña aporlaoferta.com'
WHERE `EMT_ID`=4