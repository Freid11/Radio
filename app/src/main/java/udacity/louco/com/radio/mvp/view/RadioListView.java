package udacity.louco.com.radio.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import udacity.louco.com.radio.mvp.model.object.Radio;

public interface RadioListView extends MvpView{

    @StateStrategyType( value = AddToEndSingleStrategy.class)
    void showsRadio(List<Radio> radioList);

    void addRadio(Radio radio);

    void playRadio(Radio radio);

    @StateStrategyType( value = AddToEndSingleStrategy.class)
    void showInfoRadio(Radio radio);

    @StateStrategyType( value = AddToEndSingleStrategy.class)
    void showFavoriteState(Radio radio);

    @StateStrategyType( value = AddToEndSingleStrategy.class)
    void showPlayerState(Integer state);

}
