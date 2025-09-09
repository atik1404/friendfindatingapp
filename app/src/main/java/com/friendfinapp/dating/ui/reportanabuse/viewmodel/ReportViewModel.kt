package com.friendfinapp.dating.ui.reportanabuse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.ui.mainrepo.MainRepo
import com.friendfinapp.dating.ui.reportanabuse.ReportAnAbuse
import com.friendfinapp.dating.ui.reportanabuse.model.ReportAbuseResponseModel
import com.friendfinapp.dating.ui.reportanabuse.model.ReportAbusedPostingModel

class ReportViewModel : ViewModel() {


    private val repo = MainRepo()
    fun reportAnAbuse(
        model: ReportAbusedPostingModel,
        customDialog: ProgressCustomDialog?,
        reportAnAbuse: ReportAnAbuse
    ) : LiveData<ReportAbuseResponseModel> = repo.reportAnAbuse(model,customDialog,reportAnAbuse)

}