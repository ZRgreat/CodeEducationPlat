package sdu.codeeducationplat.model.enums;

public enum ApplicationStatus {
    PENDING,   // 待审核（用户提交申请后，默认状态）
    APPROVED,  // 申请通过（用户变为 TEACHER）
    REJECTED,  // 申请被拒绝（用户仍为 STUDENT，可重新申请）
    WITHDRAWN, // 用户主动撤回申请
    EXPIRED    // 申请超时未审核（可选，如果有时间限制）
}

