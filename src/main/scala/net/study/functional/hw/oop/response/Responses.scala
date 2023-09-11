package net.study.functional.hw.oop.response

import net.study.functional.hw.oop.dto.SignUpDto
import net.study.functional.hw.oop.processor.Status


case class SignUpResponse(status: Status, dto: SignUpDto)

case class SignInResponse()

case class WorkInfoResponse()
