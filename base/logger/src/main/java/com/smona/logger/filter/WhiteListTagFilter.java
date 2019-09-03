package com.smona.logger.filter;

public class WhiteListTagFilter implements IFilter {
    String[] whiteListTags;

    @Override
    public boolean accept(String tag) {
        if (whiteListTags!=null&&whiteListTags.length>0){

            for (int i=0;i<whiteListTags.length;i++){

                String whiteListTag = whiteListTags[i];
                if (whiteListTag.equals(tag)){
                    return true;
                }
            }
        }
        return false;
    }

    public  WhiteListTagFilter(String ... acceptedTags ){
        whiteListTags=acceptedTags;
    }

}
