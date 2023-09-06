package net.study.functional.hw.oop.dto

import net.study.functional.hw.oop.request.Experience


/// DTOs
case class SignUpDto(
                      name: String,
                      surname: String,
                      login: String,
                      secret: String,
                      msisdn: String
                    )

case class SignInDto(
                      login: String,
                      secret: String
                    )

case class WorkInfoInDto(
                          login: String,
                          workExperience: List[Experience],
                          certificates: List[String]
                        )


