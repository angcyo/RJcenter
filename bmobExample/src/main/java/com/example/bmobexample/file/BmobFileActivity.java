package com.example.bmobexample.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.smile.filechoose.api.ChooserType;
import com.smile.filechoose.api.ChosenFile;
import com.smile.filechoose.api.FileChooserListener;
import com.smile.filechoose.api.FileChooserManager;

/** 文件上传+批量更新数据
  * @ClassName: BmobFileActivity
  * @Description: TODO
  * @author smile
  * @date 2014-5-22 下午7:58:58
  * 
  * 文件上传包含以下四种形式：（单一文件上传或文件批量上传）
  * 
  * 1、插入单条数据（该数据只有一个BmobFile列）
  * 2、批量插入多条数据-且每条数据都存在一个BmobFile列
  * 3、插入单条数据（多个BmobFile列）
  * 4、批量插入多条数据-且每条数据都存在多个BmobFile列
  * 
  */
@SuppressLint("SdCardPath")
public class BmobFileActivity extends BaseActivity implements OnClickListener, FileChooserListener{

	Button tv_one_one;
	Button tv_one_many;
	Button tv_many_one;
	Button tv_many_many;
	Button btn_delete;

	private FileChooserManager fm;
	
	TextView tv_path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file);
		initViews();
		initListeners();
	}

	public void initViews() {
		// TODO Auto-generated method stub
		tv_one_one = (Button) findViewById(R.id.tv_one_one);
		tv_one_many = (Button) findViewById(R.id.tv_one_many);
		tv_many_one = (Button) findViewById(R.id.tv_many_one);
		tv_many_many = (Button) findViewById(R.id.tv_many_many);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		tv_path = (TextView)findViewById(R.id.tv_path);
	}

	public void initListeners() {
		// TODO Auto-generated method stub
		tv_one_one.setOnClickListener(this);
		tv_one_many.setOnClickListener(this);
		tv_many_one.setOnClickListener(this);
		tv_many_many.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
	}
	
	 public void pickFile() {
        fm = new FileChooserManager(this);
        fm.setFileChooserListener(this);
        try {
            fm.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	 
	 
	@Override
	public void onError(String arg0) {
		// TODO Auto-generated method stub
		showToast(arg0);
	}

	ChosenFile choosedFile;
	
	@Override
	public void onFileChosen(final ChosenFile file) {
		// TODO Auto-generated method stub
		choosedFile = file;
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	showFileDetails(file);
            	File mp3 = new File(choosedFile.getFilePath());
            	uploadMovoieFile(mp3);
            }
        });
	}
		
	private void showFileDetails(ChosenFile file) {
        StringBuffer text = new StringBuffer();
        text.append("File name: " + file.getFileName() + "\n");
        text.append("File path: " + file.getFilePath() + "\n");
        text.append("File size: " + file.getFileSize() );
        tv_path.setText(text.toString());
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooserType.REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            if (fm == null) {
                fm = new FileChooserManager(this);
                fm.setFileChooserListener(this);
            }
            Log.i(TAG, "Probable file size: " + fm.queryProbableFileSize(data.getData(), this));
            fm.submit(requestCode, data);
        }
    }
	 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_one_one://插入单条数据（一个BmobFile列）
			insertDataWithOne();
			break;
		case R.id.tv_one_many://插入单条数据（多个BmobFile列）
			insertDataWithMany();
			break;
		case R.id.tv_many_one://批量插入多条数据-且每条数据都存在一个BmobFile列
			insertBatchDatasWithOne();
			break;
		case R.id.tv_many_many://批量插入多条数据-且每条数据都存在多个BmobFile列
			insertBatchDatasWithMany();
			break;
		case R.id.btn_delete://
			deleteFile();
			break;
		}
	}
	
	private void deleteFile(){
		BmobFile file = new BmobFile();
		file.setUrl(url);
		file.delete(this, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("文件删除成功");
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				showToast("文件删除失败："+code+",msg = "+msg);
			}
		});
	}
	
	//======================单个BmobFile列=======================================
	
	/** 插入单条数据（单个BmobFile列）
	  * 例如：插入单条电影
	  * @return void
	  * @throws
	  */
	private void insertDataWithOne(){
		if(choosedFile ==null){
			showToast("请先选择文件");
			pickFile();
			return;
		}
	}
	
	private static String url="";
	
	ProgressDialog dialog =null;
	
	/** 上传指定路径下的电影文件
	  * @Description: TODO
	  * @param @param type
	  * @param @param i
	  * @param @param file 
	  * @return void
	  * @throws
	  */
	private void uploadMovoieFile(File file) {
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);                 
		dialog.setTitle("上传中...");
		dialog.setIndeterminate(false);               
		dialog.setCancelable(true);       
		dialog.setCanceledOnTouchOutside(false);  
		dialog.show();
		final BmobFile bmobFile = new BmobFile(file);
		bmobFile.upload(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				url = bmobFile.getUrl();
				showToast("文件上传成功");
				Log.i("life", "电影文件上传成功，返回的名称--"+bmobFile.getFileUrl(BmobFileActivity.this)+"，文件名="+bmobFile.getFilename());
				insertObject(new Movie("冰封：重生之门",bmobFile));
			}

			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub
				Log.i("life", "uploadMovoieFile-->onProgress:"+arg0);
				dialog.setProgress(arg0);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showToast("-->uploadMovoieFile-->onFailure:" + arg0+",msg = "+arg1);
			}

		});

	}
	
	//============================================
	/**
	 * 注：以下的测试文件路径仅供测试所用，程序若完整运行，请自行替换成sd卡内部文件路径
	 */
	List<BmobObject> movies = new ArrayList<BmobObject>();
	String filePath_mp3 = "sdcard/png0.png";
	String filePath_lrc = "sdcard/test1.jpg";
	
	/**
	  * 此方法适用于批量更新数据且每条数据只有一个BmobFile字段
	  * 例如：批量上传电影Movies
	  * @Title: insertBatchDatasWithOne
	  * @throws
	  */
	public void insertBatchDatasWithOne(){
		String[] filePaths = new String[2];
		filePaths[0] = filePath_mp3;
		filePaths[1] = filePath_lrc;
		//批量上传是会依次上传文件夹里面的文件
		Bmob.uploadBatch(this, filePaths, new UploadBatchListener() {
			
			@Override
			public void onSuccess(List<BmobFile> files,List<String> urls) {
				// TODO Auto-generated method stub
				Log.i("life","insertBatchDatasWithOne -onSuccess :"+urls.size()+"-----"+files+"----"+urls);
				if(urls.size()==1){//如果第一个文件上传完成
					Movie movie =new Movie("哈利波特1",files.get(0));
					movies.add(movie);
				}else if(urls.size()==2){//第二个文件上传成功
					Movie movie1 =new Movie("哈利波特2",files.get(1));
					movies.add(movie1);
					insertBatch(movies);
				}
			}
			
			@Override
			public void onError(int statuscode, String errormsg) {
				// TODO Auto-generated method stub
				showToast("错误码："+statuscode +",错误描述："+errormsg);
			}

			@Override
			public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
				// TODO Auto-generated method stub
				Log.i("life","insertBatchDatasWithOne -onProgress :"+curIndex+"---"+curPercent+"---"+total+"----"+totalPercent);
				
			}
		});
		
	
	}
	

	/** 创建操作
	  * insertObject
	  * @return void
	  * @throws
	  */
	private void insertObject(final BmobObject obj){
		obj.save(BmobFileActivity.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("-->创建数据成功：" + obj.getObjectId());
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("-->创建数据失败：" + arg0+",msg = "+arg1);
			}
		});
	}
	
	//======================多个BmobFile列=======================================
	
	List<BmobObject> songs = new ArrayList<BmobObject>();
	
	/**
	 * 插入单条数据（多个BmobFile列--以两个为例）
	 * 例：上传MP3文件和歌词lrc文件到一条Song数据中
	 */
	private void insertDataWithMany() {
		String[] filePaths = new String[2];
		filePaths[0] = filePath_mp3;
		filePaths[1] = filePath_lrc;
		Bmob.uploadBatch(this, filePaths, new UploadBatchListener() {
			
			@Override
			public void onSuccess(List<BmobFile> files,List<String> urls) {
				// TODO Auto-generated method stub
				Log.i("life","insertDataWithMany -onSuccess :"+urls.size()+"-----"+files+"----"+urls);
				if(urls.size()==2){//如果全部上传完，则更新该条记录
					Song song =new Song("汪峰0","北京北京0",files.get(0),files.get(1));
					insertObject(song);
				}else{
					//有可能上传不完整，中间可能会存在未上传成功的情况，你可以自行处理
				}
			}
			@Override
			public void onError(int statuscode, String errormsg) {
				// TODO Auto-generated method stub
				showToast("错误码"+statuscode +",错误描述："+errormsg);
			}
			@Override
			public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
				// TODO Auto-generated method stub
				Log.i("life","insertDataWithMany -onProgress :"+curIndex+"---"+curPercent+"---"+total+"----"+totalPercent);
			}
		});
		
	}

	/**
	  * 此方法适用于批量更新数据且每条数据都有多个BmobFile字段：
	  * 例如：批量上传歌曲songs
	  * @Title: insertBatchDatasWithOne
	  * @throws
	  */
	private void insertBatchDatasWithMany() {
		File ff = new File("/mnt/sdcard/testbmob/");
		File[] fs = ff.listFiles();
		String[] filePaths = new String[fs.length];
		if(fs!=null && fs.length>0){
			final int len = fs.length;
			for(int i=0;i<len;i++){
				filePaths[i] = fs[i].getAbsolutePath();
			}
			Bmob.uploadBatch(this, filePaths, new UploadBatchListener() {
				
				@Override
				public void onSuccess(List<BmobFile> files,List<String> urls) {
					// TODO Auto-generated method stub
					Log.i("life","insertBatchDatasWithMany -onSuccess :"+urls.size()+"-----"+files+"----"+urls);
					if(urls.size()==len){//如果全部上传完，则批量更新
						Log.i("life","====insertBatch=====");
						//因为我的文件夹下面有四种图片每两个插入到一条数据中
						Song song =new Song("汪峰0","北京北京0",files.get(0),files.get(1));
						songs.add(song);
//						Song song1 =new Song("汪峰1","北京北京1",files.get(2),files.get(3));
//						songs.add(song1);
						//批量插入操作
						insertBatch(songs);
					}else{
						//有可能上传不完整，中间可能会存在未上传成功的情况，你可以自行处理
					}
				}
				
				@Override
				public void onError(int statuscode, String errormsg) {
					// TODO Auto-generated method stub
					showToast("错误码"+statuscode +",错误描述："+errormsg);
				}

				@Override
				public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
					// TODO Auto-generated method stub
					Log.i("life","insertBatchDatasWithMany -onProgress :"+curIndex+"---"+curPercent+"---"+total+"----"+totalPercent);
				}
			});
		}
	}
	
	/** 批量插入操作
	  * insertBatch
	  * @return void
	  * @throws
	  */
	public void insertBatch(List<BmobObject> files){
		new BmobObject().insertBatch(BmobFileActivity.this, files, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("---->批量更新成功");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("---->批量更新失败"+arg0);
				
			}
		});
	}

}
