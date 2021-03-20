package com.example.cycletracker.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cycletracker.data.DataRepository;
import com.example.cycletracker.home.dagger.HomeScope;
import com.example.cycletracker.home.model.Bicycle;
import com.example.cycletracker.model.Result;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

@HomeScope
public class BicycleViewModel extends ViewModel {

    private DataRepository dataRepository;
    private ExecutorService executorService;

    MutableLiveData<Bicycle> cycleMutableLiveData = new MutableLiveData<>();

    @Inject
    BicycleViewModel(DataRepository dataRepository, ExecutorService executorService) {
        this.dataRepository = dataRepository;
        this.executorService = executorService;
    }

    public LiveData<Bicycle> getCycleLiveData() {
        return cycleMutableLiveData;
    }

    public void bookCycle(String qrcode, String authToken) {
        executorService.submit(()->{
            Result<Bicycle> res = dataRepository.bookCycle(qrcode, authToken);
            if(res instanceof Result.Success) {
                cycleMutableLiveData.postValue(((Result.Success<Bicycle>) res).getData());
            } else {
                cycleMutableLiveData.postValue(null);
            }
        });
    }
}
