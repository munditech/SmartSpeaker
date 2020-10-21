package tk.munditv.smartspeaker.voicerecognize;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.LocaleList;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import ai.olami.android.tts.ITtsPlayerListener;
import ai.olami.android.tts.TtsPlayer;
import ai.olami.cloudService.APIConfiguration;
import ai.olami.cloudService.APIResponse;
import ai.olami.cloudService.TextRecognizer;
import ai.olami.nli.DescObject;
import ai.olami.nli.NLIResult;
import ai.olami.util.GsonFactory;
import tk.munditv.smartspeaker.application.OlamiConfig;

public class SmartSpeakerService extends Service {

    private final static String TAG = "SmartSpeakerService";

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private Gson mJsonDump;
    private TextRecognizer mRecognizer;
    private AudioManager audioManager;
    private static Context context;
    private ArrayList<String> data;

    TtsPlayerListener mTtsPlayerListener = null;
    TtsPlayer mTtsPlayer = null;

    private float mSpeed = 1.0f;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initialize();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void initialize() {
        Log.d(TAG, "initialize(0 start!");
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this,
                ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.i(TAG, "--------onReadyForSpeech()---------");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.i(TAG, "--------onBeginningOfSpeech()---------");
            }

            @Override
            public void onRmsChanged(float v) {
                Log.i(TAG, "--------onRmsChanged()---------");
                //startSpeech();
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.i(TAG, "--------onBufferReceived()---------");
            }

            @Override
            public void onEndOfSpeech() {
                Log.i(TAG, "--------onEndOfSpeech()---------");
                stopSpeech();
                mHandler.postDelayed(restartSpeech, 3000);
            }

            @Override
            public void onError(int i) {
                Log.i(TAG, "--------onError()---------");
                //stopSpeech();
                //startSpeech();

            }

            @Override
            public void onResults(Bundle bundle) {
                Log.i(TAG, "--------onResults()---------");
                //stopSpeech();
                data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d(TAG, "result = " + data.get(0));
                processNLIAnalysis(data.get(0));
                //speechRecognizer.startListening(speechRecognizerIntent);
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.i(TAG, "--------onPartialResults()---------");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.i(TAG, "--------onEvent()---------");
            }
        });

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // for Olami Initialize
        String appKey = OlamiConfig.getAppKey();
        String appSecret = OlamiConfig.getAppSecret();
        // Set default localization setting by the setting of system language.
        String systemLanguage = getSystemLanguage();
        if (systemLanguage.equals("zh-TW")) {
            OlamiConfig.setLocalizeOption(APIConfiguration.LOCALIZE_OPTION_TRADITIONAL_CHINESE);
        } else {
            OlamiConfig.setLocalizeOption(APIConfiguration.LOCALIZE_OPTION_SIMPLIFIED_CHINESE);
        }
        mJsonDump = GsonFactory.getDebugGson(false);

        // * Step 1: Configure your key and localize option.
        APIConfiguration config = new APIConfiguration(
                OlamiConfig.getAppKey(), OlamiConfig.getAppSecret(), OlamiConfig.getLocalizeOption());

        // * Step 2: Create the text recognizer.
        mRecognizer = new TextRecognizer(config);
        mRecognizer.setSdkType("android");

        // * Optional steps: Setup some other configurations.
        mRecognizer.setEndUserIdentifier("Someone");
        mRecognizer.setTimeout(10000);

        // Initial listener
        mTtsPlayerListener = new TtsPlayerListener();
        // Initial TTS player
        mTtsPlayer = new TtsPlayer(this, mTtsPlayerListener);

        speechRecognizer.startListening(speechRecognizerIntent);
    }

    private Handler mHandler = new Handler();

    private class TtsPlayerListener implements ITtsPlayerListener {
        @Override
        public void onPlayEnd() {
            Log.i(TAG, "--------onPlayEnd()---------");
            //startSpeech();
            mHandler.postDelayed(restartSpeech, 1000);
        }

        @Override
        public void onStop() {
            Log.i(TAG, "--------onStop()---------");
        }

        @Override
        public void onPlayingTTS(String TTSString) {
            Log.i(TAG, "--------onPlayingTTS()---------"+ TTSString);
        }
    }

    private void TTSPlayStart(@NonNull String str) {
        // * Set up TTS playback speed.
        mTtsPlayer.setSpeed(mSpeed);

        // * Set up TTS output volume.
        mTtsPlayer.setVolume(100);

        // * Play TTS by the specified text.
        mTtsPlayer.playText(str, true);
    }

    protected String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }

        String language = locale.getLanguage() + "-" + locale.getCountry();
        return language;
    }

    private void startSpeech() {
        Log.d(TAG, "startSpeech()");
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    private void stopSpeech() {
        Log.d(TAG, "stopSpeech()");
        speechRecognizer.stopListening();
        //mHandler.postDelayed(restartSpeech, 5000);
    }

    private Runnable restartSpeech = new Runnable() {

        @Override
        public void run() {
            startSpeech();
        }
    };

    private void processNLIAnalysis(final String textInput) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // * Send text to request NLI recognition.
                    APIResponse response = mRecognizer.requestNLI(textInput);
                    //
                    // You can also send text with NLIConfig to append "nli_config" JSON object.
                    //
                    // For Example,
                    // try to replace 'requestNLI(inputText)' with the following sample code:
                    // ===================================================================
                    // NLIConfig nliConfig = new NLIConfig();
                    // nliConfig.setSlotName("myslot");
                    // APIResponse response = mRecognizer.requestNLI(textInputEdit.getText().toString(), nliConfig);
                    // ===================================================================
                    //

                    // Check request status.
                    if (response.ok() && response.hasData()) {
                        // * Dump NLI results by JSON format.
                        //textInputAPIResponseChangeHandler(mJsonDump.toJson(response));
                        NLIResult[] res = response.getData().getNLIResults();
                        for (NLIResult nliResult : res) {
                            DescObject descObject = nliResult.getDescObject();
                            Log.d(TAG, " NLI Response results : " +descObject.getReplyAnswer());
                            TTSPlayStart(descObject.getReplyAnswer());
                        }
                    }
                    //startSpeech();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
