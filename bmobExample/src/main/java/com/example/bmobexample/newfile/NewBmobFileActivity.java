package com.example.bmobexample.newfile;

import java.util.Arrays;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BTPFileResponse;
import com.bmob.BmobPro;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.bmob.btp.callback.DownloadListener;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.bmob.btp.callback.ThumbnailListener;
import com.bmob.btp.callback.UploadBatchListener;
import com.bmob.btp.callback.UploadListener;
import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.example.bmobexample.bean.Person;
import com.smile.filechoose.api.ChooserType;
import com.smile.filechoose.api.ChosenFile;
import com.smile.filechoose.api.FileChooserListener;
import com.smile.filechoose.api.FileChooserManager;

/**
 * @ClassName: NewBmobFileActivity
 * @Description: TODO
 * @author smile
 * @date 2014-10-24 下午2:33:40
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class NewBmobFileActivity extends BaseActivity implements FileChooserListener{

	protected ListView mListview;
	protected BaseAdapter mAdapter;
	private FileChooserManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_file);
		initListView();
	}

	private void initListView(){
		mListview = (ListView) findViewById(R.id.list);
		mAdapter = new ArrayAdapter<String>(this, R.layout.item_list,R.id.tv_item, getResources().getStringArray(
						R.array.bmob_newfile_arrays));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				testBmobPro(position + 1);
			}
		});
	}

	ChosenFile choosedFile;
	
	@Override
	public void onFileChosen(final ChosenFile file) {
		// TODO Auto-generated method stub
		choosedFile = file;
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	upload();
            }
        });
	}
	
	@Override
	public void onError(String arg0) {
		// TODO Auto-generated method stub
		showToast(arg0);
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
	
	public void pickFile() {
        fm = new FileChooserManager(this);
        fm.setFileChooserListener(this);
        try {
            fm.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void testBmobPro(int pos) {
		switch (pos) {
		case 1:
			showToast("请先选择文件");
			pickFile();
			break;
		case 2:
			uploadBatchFile();
			break;
		case 3:
			download();
			break;
		case 4:
			clearCache();
			showToast("缓存清理成功");
			break;
		case 5:
			getCurrentCache();
			break;
		case 6:
			showToast("文件下载地址："+BmobPro.getInstance(this).getCacheDownloadDir());
			break;
		case 7:
			requestThumbnailTask();
			break;
		case 8:
			startActivity(new Intent(this, LocalThumbnailActivity.class));
			break;
		case 9: //获取文件的可访问地址
			getAccessURL();
			break;
		case 10://删除文件
			deleteFile();
			break;
		default:
			break;
		}
	}

	String downLoadUrl = "";
	/**
	 * @Description:单一文件上传
	 * @param  
	 * @return void
	 * @throws
	 */
	ProgressDialog dialog =null;

	private void upload(){
		dialog = new ProgressDialog(NewBmobFileActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);                 
		dialog.setTitle("上传中...");
		dialog.setIndeterminate(false);               
		dialog.setCancelable(true);       
		dialog.setCanceledOnTouchOutside(false);  
		dialog.show();
		BTPFileResponse response = BmobProFile.getInstance(NewBmobFileActivity.this).upload(choosedFile.getFilePath(), new UploadListener() {

			@Override
			public void onSuccess(String fileName,String url,BmobFile file) {
				// TODO Auto-generated method stub
				Log.i("smile", "新版文件服务的fileName = "+fileName+",新版文件服务的url ="+url);
				if(file!=null){
					Log.i("smile", "兼容旧版文件服务的源文件名 = "+file.getFilename()+",文件地址url = "+file.getUrl());
				}
				downloadName = fileName;
				dialog.dismiss();
				//保存BmobFile对象
				Person person = new Person();
				person.setPic(file);
				person.setName("smile");
				person.save(NewBmobFileActivity.this,new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Log.i("smile", "Person对象保存成功");
					}
					
					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						Log.i("smile", "Person对象保存失败："+code+"-"+msg);
					}
				});
				showToast("文件已上传成功："+fileName);
			}

			@Override
			public void onProgress(int ratio) {
				// TODO Auto-generated method stub
				Log.i("smile","MainActivity -onProgress :"+ratio);
				dialog.setProgress(ratio);
			}

			@Override
			public void onError(int statuscode, String errormsg) {
				// TODO Auto-generated method stub
				//showLog("MainActivity -onError :"+statuscode +"--"+errormsg);
				dialog.dismiss();
				showToast("上传出错："+errormsg);
			}
		});

		showLog("upload方法返回的code = "+response.getStatusCode());
	}

	/**
	 * @Title: updateBatchFile
	 * @Description: 文件批量上传
	 * @param  
	 * @return void
	 * @throws
	 */
	private void uploadBatchFile(){
		dialog = new ProgressDialog(NewBmobFileActivity.this);
		String[] files = new String[]{"sdcard/png0.png","sdcard/png1.png"};
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);                 
		dialog.setTitle("批量上传中...");
		dialog.setIndeterminate(false);               
		dialog.setCancelable(true);       
		dialog.setCanceledOnTouchOutside(false);  
		dialog.setMax(files.length);
		dialog.show();
		BmobProFile.getInstance(NewBmobFileActivity.this).uploadBatch(files, new UploadBatchListener() {

			@Override
			public void onSuccess(boolean isFinish,String[] fileNames,String[] urls,BmobFile[] files) {
				// TODO Auto-generated method stub
				if(isFinish){
					dialog.dismiss();
				}
				showToast(""+Arrays.asList(fileNames)+""+Arrays.asList(files));
				showLog("NewBmobFileActivity -onSuccess :"+isFinish+"-----"+Arrays.asList(fileNames)+"----"+Arrays.asList(urls)+"----"+Arrays.asList(files));
			}

			@Override
			public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
				// TODO Auto-generated method stub
				dialog.setProgress(curIndex);
				showLog("NewBmobFileActivity -onProgress :"+curIndex+"---"+curPercent+"---"+total+"----"+totalPercent);
			}

			@Override
			public void onError(int statuscode, String errormsg) {
				// TODO Auto-generated method stub
				showLog("NewBmobFileActivity -onError :"+statuscode+"--"+errormsg);
				dialog.dismiss();
				showToast("批量上传出错："+errormsg);
			}
		});
	}

	private static String downloadName= "";

	/**
	 * @Description: 文件下载
	 * @param  
	 * @return void
	 * @throws
	 */
	private void download(){
		if(downloadName.equals("")){
			showLog("请指定下载文件名");
			return;
		}
		dialog = new ProgressDialog(NewBmobFileActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);                 
		dialog.setTitle("下载中...");
		dialog.setIndeterminate(false);               
		dialog.setCancelable(true);       
		dialog.setCanceledOnTouchOutside(false);  
		dialog.show();
		BmobProFile.getInstance(NewBmobFileActivity.this).download(downloadName, new DownloadListener() {

			@Override
			public void onSuccess(String fullPath) {
				// TODO Auto-generated method stub
				showLog("MainActivity -download-->onSuccess :"+fullPath);
				dialog.dismiss();
				showToast("下载成功："+fullPath);
			}

			@Override
			public void onProgress(String localPath, int percent) {
				// TODO Auto-generated method stub
				showLog("MainActivity -download-->onProgress :"+percent);
				dialog.setProgress(percent);
			}

			@Override
			public void onError(int statuscode, String errormsg) {
				// TODO Auto-generated method stub
				showLog("MainActivity -download-->onError :"+statuscode +"--"+errormsg);
				dialog.dismiss();
				showToast("下载出错："+errormsg);
			}
		});
	}

	/**
	 * @Description: 提交请求服务器生成缩略图的任务
	 * @param  
	 * @return void
	 * @throws
	 */
	private void requestThumbnailTask(){
		BmobProFile.getInstance(NewBmobFileActivity.this).submitThumnailTask(downloadName, 1, new ThumbnailListener() {

			@Override
			public void onSuccess(String thumbnailName,String thumbnailUrl) {
				// TODO Auto-generated method stub
				//此缩略图名称和地址并不一定是及时返回的，此为异步方法
				showToast("提交请求服务器生成缩略图的任务成功：:"+thumbnailName+"-->"+thumbnailUrl);
				showLog("MainActivity -onSuccess :"+thumbnailName+"-->"+thumbnailUrl);
			}

			@Override
			public void onError(int statuscode, String errormsg) {
				// TODO Auto-generated method stub
				showToast("提交请求服务器生成缩略图的任务失败：:"+statuscode+"---"+errormsg);
				showLog("MainActivity -onError :"+statuscode+"---"+errormsg);
			}
		});
	}
	
	private void getAccessURL(){
		BmobProFile.getInstance(this).getAccessURL(downloadName, new GetAccessUrlListener() {
			
			@Override
			public void onError(int errorcode, String errormsg) {
				// TODO Auto-generated method stub
				showToast("获取文件的服务器地址失败："+errormsg+"("+errorcode+")");
			}
			
			@Override
			public void onSuccess(BmobFile file) {
				// TODO Auto-generated method stub
				Log.i("smile", "源文件名："+file.getFilename()+"，可访问的地址："+file.getUrl());
				showToast("获取文件的服务器地址成功：源文件名："+file.getFilename()+"，group="+file.getGroup()+"，fileUrl = "+file.getUrl());
			}
		});
	}
	
	private void deleteFile(){
		BmobProFile.getInstance(this).deleteFile(downloadName, new DeleteFileListener() {
			
			@Override
			public void onError(int errorcode, String errormsg) {
				// TODO Auto-generated method stub
				showToast("删除文件失败："+errormsg+"("+errorcode+")");
			}
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("删除文件成功");
			}
		});
	}
	
	/**
	 * @Title: clearCache
	 * @Description: 清除缓存
	 * @param  
	 * @return void
	 * @throws
	 */
	public void clearCache(){
		BmobPro.getInstance(NewBmobFileActivity.this).clearCache();
	}

	/**
	 * 获取当前缓存大小
	 */
	public void getCurrentCache(){
		String cacheSize = String.valueOf(BmobPro.getInstance(NewBmobFileActivity.this).getCacheFileSize());
		String formatSize = BmobPro.getInstance(NewBmobFileActivity.this).getCacheFormatSize();
		showToast("已缓存大小："+cacheSize+"----->"+formatSize);
	}

}
