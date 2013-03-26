package com.example.gdg_opensource_codelab_sample_1;

import java.util.HashMap;

/**
 * POJO data class to represent Youtube Video Resoruce json structure.
 * refer: https://developers.google.com/youtube/v3/docs/videos#resource
 * use only id & snippet part. 
 */
public class JsonVideoResource {
    
    private static int mMockCount = 0;

    /**
     * for test purpose only.
     * @return mock JsonVideoResource Object.
     */
    public static JsonVideoResource generateMock() {
        
        JsonVideoResource res = new JsonVideoResource();
        res.id = "";
        res.snippet = new Snippet();
        
        ++mMockCount;
        res.snippet.title = "Mock #" + mMockCount;
        res.snippet.description= "Some description will be placed here...";
        
        Thumbnail thumb = new Thumbnail();
        thumb.height = 100;
        thumb.width = 100;
        thumb.url = "https://i.ytimg.com/vi/VuC0i4xTyrI/default.jpg";
        
        res.snippet.thumbnails = new HashMap<String, JsonVideoResource.Thumbnail>();
        res.snippet.thumbnails.put(String.valueOf(mMockCount), thumb);
        return res;
    }

    public String id;
    public Snippet snippet;
    
    public static class Snippet {
        
        public String title;
        public String description;
        public HashMap<String, Thumbnail> thumbnails;
        
    }//end of inner class
    
    public static class Thumbnail {
        
        String url;
        int width;
        int height;
        
    }//end of inner class
    
}//end of class
