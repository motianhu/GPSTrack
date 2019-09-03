package com.smona.logger.filter;

public class BlackListTagFilter implements IFilter {
    String[] blackListTags;

    @Override
    public boolean accept(String targetTag) {
        if (blackListTags !=null&& blackListTags.length>0){

            for (int i = 0; i< blackListTags.length; i++){
                String whiteListTag = blackListTags[i];

                if (whiteListTag.equals(targetTag)){
                    return false;
                }
            }

        }

        return true;
    }

    public BlackListTagFilter(String ... rejectedTags ){
        blackListTags =rejectedTags;
    }

}
