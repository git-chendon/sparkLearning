/**
  * @Author xu.xiaojing
  * @Date 2019/7/11 23:53
  * @Email xu.xiaojing@frontsurf.com
  * @Description
  */
@SerialVersionUID(99L)
case class JtySportDataReportMonthOfStudent(school_id: String,
                                            grade: Int,
                                            class_id: String,
                                            course_type_first_id: String,
                                            course_type_second_id: String,
                                            course_type_third_id: String,
                                            student_id: String,
                                            sex: Int,
                                            date: String,
                                            aveHeartRate: Double,
                                            aveSteps: Double,
                                            aveCalorie: Double,
                                            aveExcerciseIntensity: BigDecimal,
                                            aveExcerciseDensity: BigDecimal
                                           ) {

}
